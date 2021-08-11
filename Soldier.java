import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
* This class extends class {@link Person} and represents a soldier clubber.
* In addition to the inherited characteristics of the Person the soldier has a personalNum field.
* This class implements all abstract methods from {@link ClubAbstractEntity}
*/
public class Soldier extends Person
{
	private JTextField personalNumField;
	private String personalNum;
	
	/**
	* Parameterless constructor - creates a Soldiers fields rows using {@link ClubAbstractEntity#createRow} method
	* adds them to the center panel using  {@link ClubAbstractEntity#addToCenter} method,
	* and initializes the fields instance variables to empty values.
	* disables the cancel button until the first commit.
	* sets window size and title.
	*/
	public Soldier()
	{
		this("", "", "", "", "");
	}

	/** 
	* Constructor with five string parameters corresponding to the fields.
	* creates and initializes the instance variables of this class.
	* creates and initializes the rows by using {@link ClubAbstractEntity#createRow} method and 
	* adds them the center panel using {@link ClubAbstractEntity#addToCenter} method.
	* Setting window size and title.
	*
	* @param id person id
	* @param name person name
	* @param surname person last name
	* @param tel person phone number
	* @param personalNum personal number
	*/	
	public Soldier(String id, String name, String surname, String tel, String personalNum)
	{
		super(id, name, surname, tel);
		
		this.personalNum = personalNum;
		personalNumField = new JTextField(personalNum, 30);
		addToCenter(createRow("Personal No.",personalNumField));
			
		setTitle("Soldier Clubber's Data");
		setSize(450, 250);
	}

	/**
	* This method overrides {@link ClubAbstractEntity#match} method and indicates whether or not 
	* the key equals to the person id or soldier personalNum field of the current soldier.
	* In-order to match the personalNum the key must match the digits (after the / character)
	*
	* @param key key parameter
	* @return true if the key or personal num is equal to corresponding fields otherwise return false
	*/	
	@Override
    public  boolean match(String key)
    {//if before first commit personalNum can be empty field
    	return super.match(key) || (!personalNum.equals("") && personalNum.substring(2).equals(key));
    }
    
		/**
	* This method is a collaborative override of {@link Person#validateData} the method indicates whether 
	* the data in the fields is valid or not.
	* In addition to the regular expressions used to validate the {@link Person} fields,
	* this method checks the Soldiers added field personalNum with the regular expresion - "[ROC]/[1-9]\\d{6}"
	* The fields are checked consecutively until either the first error or all fields are found valid. 
	* Invokes {@link ClubAbstractEntity#setError} that add or remove '*' in the corresponding field to indicate valid or error field.
	*
	* @return true if fields are found valid otherwise returns false. 
	*/ 
    @Override
    protected  boolean validateData()
    {
    	if(!(super.validateData()))
    		return false;
    	
    	if(personalNumField.getText().matches("[ROC]/[1-9]\\d{6}"))
    		setError(true, personalNumField);
    	else
    	{
    		setError(false, personalNumField);
    		return false;
    	}
    	return true;
    }
    
    /**
	* This method is a collaborative override of {@link Person#commit} method.
	* The method invokes commit function from {@link Person}
	* that stores the Person information from the textFields to the corresponding person value fields
	* and then stores the personalNum in the corresponding value field.
	*/   
    @Override
    protected  void commit()
    {
    	super.commit();
    	personalNum = personalNumField.getText();
    }
   
    /**
	* This method is a collaborative override of {@link Person#rollBack} method.
	* The method invokes the commit function from {@link Person}
	* returns the values from the persons value fields to the corresponding textFields,
	* and clears all erroneous signs from the fields using {@link ClubAbstractEntity#setError} method
	* and then does the same with the added personalNum field.
	*/    
    @Override
    protected  void rollBack()
    {
    	super.rollBack();
    	personalNumField.setText(personalNum);
    	setError(true, personalNumField);
    }

}