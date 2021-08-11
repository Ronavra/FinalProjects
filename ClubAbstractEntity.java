import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
* This class is abstract class.
* It is the main entity from which clubber entities entities {@link Person} , {@link Student} , {@link Soldier} inherit.
* The class inherits from {@link JFrame} has a center panel and two buttons in the bottom of the frame OK and Cancel.
* This class responsible for the basic GUI initialization,
* The structure of data field row that is placed in the center-panel(label textField error-symbol) 
* which every subclass can add depending on the data it holds.
* The class also provides a method to add the rows and gui components to the center-Panel {@link #addToCenter}.
* This class declares 4 abstract methods {@link #match}, {@link #rollBack} , {@link #validateData} , {@link #commit}
* which need to be defined in the subclasses.
* The class handles the button events.
* pressing 'OK' checks field validity according to implementation in the subclass commits the values and hides the frame
* pressing 'Cancel' invokes value rollBack and hides the frame
*/
public abstract class ClubAbstractEntity extends JFrame implements Serializable
{
	private final JButton okButton;
	private final JButton cancelButton;
	private JPanel centerPanel;
  private ButtonHandler handler;
    
    
    /**
    * Parameterless constructor - creates and initializes the instance variables and the handler {@link ClubAbstractEntity.ButtonHandler},
    * initializes and creates the GUI elements
    * the button's functionality will be defined by the inheriting subclasses by the definition of the four abstract methods.
    * places the panel in the middle of the frame, prevents resizing and disabling the window close button.
    */
    public ClubAbstractEntity()
    {
      okButton = new JButton("OK");
      cancelButton = new JButton("Cancel");
      centerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      add(centerPanel);
      
      handler = new ButtonHandler();
      okButton.addActionListener(handler);
      cancelButton.addActionListener(handler);
      
      JPanel southPanel = new JPanel();
      southPanel.add(okButton);
      southPanel.add(cancelButton);
      add(southPanel, BorderLayout.SOUTH);
      //cancel button disabled before first commit
      cancelButton.setEnabled(false);
      setResizable(false);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
    
    
   /** 
   * This method gets GUI {@link Component} and add it to the center of the panel.
   * The panels layout is {@link FlowLayout} with right alignment.
   *
   @param guiComponent GUI component that is added to the center of the panel.
   */
    
    protected void addToCenter(Component guiComponent)
    {
    	centerPanel.add(guiComponent);
    }
    
   /**
   * Abstract method that matches clubAbstractEntity object to unique key.
   * Method is defined by each subclasses and  whether the key is a match or not.
   *
   * @param key search key
   * @return if the key match or not 
    */
    public abstract boolean match(String key); 
    
    /**
    * Abstract method to validate the objects input data.
    * Every subclass must define this method in a way
    * that indicates whether the objects TextField inputs are valid for each of its fields.
    * checks fields consecutively and stops if field is invalid and adds an error sign.
    * @return if the data is valid or not.
    */
    protected abstract boolean validateData();
    
    /**
    * Abstract method to store the objects information.
    * Every subclass must define this method,
    * by saving the information in each textField to the corresponding string-field.  
    */
    protected abstract void commit();
    
     /**
    * Abstract method that returns object to validated state. 
    * Every subclass must define this method, 
    * that returns the validated stored information to the corresponding textField.
    */
    protected abstract void rollBack();
    
    
    /**
    * This method creates a generic line structure 
    * every subclass creates the rows that correspond to its characterizing fields.
    *
    * @param label The label that identifies the rows field
    * @param field The rows text-field
    * @return the row to be added
    */
    protected JPanel createRow(String label, JTextField field)
    {
    	JPanel row = new JPanel();
      row.add(new JLabel(label));
      row.add(field);
      JLabel error = new JLabel(" ");
      error.setForeground(Color.red);
      row.add(error);
		
		  return row;
    }
    
    /**
    * This method is creates and removes the error indication to wether  or not the fields is valid.
    * The indication is a red Asterisk ('*') in the end of the corresponding row ,
    * if the field is valid the asterisk is replaced by a space.
    *
    * @param state Indication whether or not the field is valid 
    * @param field The field which the text need to be added to
    */
    protected void setError(boolean state, JTextField field)
    {
    	if(state)
			  ((JLabel)field.getParent().getComponent(2)).setText(" ");
      else
        ((JLabel)field.getParent().getComponent(2)).setText("*");
    }
 
    /**
    * This is a inner class for buttons 'OK' and 'Cancel' event handling 
    * The implementation of this handlers actionListener is generic and correct for any type of customer,
    * every subclass must implement all the abstract methods.
    * If "OK" is pressed - {@link #validateData} is invoked and if all fields are valid 
    * {@link #commit} is invoked, otherwise does nothing.
    * if "Cancel" is pressed {@link #rollBack} is invoked.
    */
    private class ButtonHandler implements ActionListener , Serializable
    {
    	@Override
    	public void actionPerformed(ActionEvent event)
    	{
    		switch(event.getActionCommand())
    		{
    		case "OK" ://checks that fields are valid and id doesn't exist
    	 		 if(validateData() && !NightClubMgmtApp.isExists(ClubAbstractEntity.this))
    				{
    					commit();
    					setVisible(false);
    					cancelButton.setEnabled(true);
    		    } 
    			break;
    		case "Cancel" :
    			rollBack();
    			setVisible(false);
    			break;
    		}
    	}
    }

}