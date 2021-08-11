import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


/**
* This class extends {@link JFrame}
* This class is the main class that creates the GUI interface for managing the night club clubbers.of communication with the users
* The GUI interface provides the user with the option to create and add new clubbers of {@link ClubAbstractEntity} type (via a create button)
* and to {@link #search} the database for an existing clubber via search button that opens a input dialog.
* This class is responsible for loading the customers data from  a binary file called  BKCustomers.dat using {@link #loadClubbersDBFromFile} when opening the app,
* and saving all existing clubbers in the file using {@link #writeClubbersDBtoFile} when closing the app.
* Customers are stored in an {@link ArrayList} data structure.
*/
public class NightClubMgmtApp extends JFrame 
{
	//Night-Club Regular Customers Repository
	private static ArrayList<ClubAbstractEntity> clubbers;
	private JComboBox clubberType;
	private JTextField size;
	
	
    /**
    * Parameterless constructor - creates and initializes the instance variables and the handler {@link NightClubMgmtApp.ButtonHandler}),
    * initializes and creates the GUI elements for the apps interface with the users.
    * assigning the action listeners to the buttons.
    * loading all the information from the file {@link #loadClubbersDBFromFile}.
    * placing the panel in the middle of the screen,
	* preventing resizing and controlling the apps closing operation using an anonymous inner class {@link WindowListener}.
    */	
	public NightClubMgmtApp()
	{
		String [] type = {"Person", "Soldier", "Student"};
		clubbers = new ArrayList<>();
		
		JPanel searchBar = new JPanel();
		JButton search = new JButton("Search");
		searchBar.add(search);
		
		JPanel createBar = new JPanel();
		clubberType = new JComboBox<>(type);
		JButton create = new JButton("Create");
		createBar.add(create);
		createBar.add(clubberType);
		
		ButtonHandler handler = new ButtonHandler();
		search.addActionListener(handler);
		create.addActionListener(handler);
		
		add(searchBar,BorderLayout.NORTH);
		add(createBar);
		
		loadClubbersDBFromFile();
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Night Club Manager App");
		setSize(450, 160);
		setVisible(true);
		//controls the closing of the app 
		//saves the clubbers data to file before closing
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e)
				{
					writeClubbersDBtoFile();
					System.exit(0);
				}
			 });
						
	}	

/**
* This method searches the clubber arrayList for existing clubber using {@link ClubAbstractEntity#match}s overridden methods,
* using a key inputted by the user.
* The match method is invoked polymorphically for the corresponding type of {@link ClubAbstractEntity} 
* {@link Person#match}, {@link Soldier#match} and {@link Student#match}. 
* @param key the customer key
* @return the customer if the search was successful otherwise null
*/
	private static ClubAbstractEntity search(String key)
	{
		//search for clubber 
		for(ClubAbstractEntity clubber : clubbers)
			if(clubber.match(key))
				return clubber;
			//if clubber not found
			return null;		
	}
	
	/**
	* This method manages the search for specific clubber in the database.
	* The method creates a dialogs messages with the user to input a search key,
	* calls {@link #search} to check if clubber exists.
	* If search returns null a message dialog is opened notifying the user that the clubber doesn't exist in the database.
	*/
	private void manipulateDB()
	{
		
		String key = JOptionPane.showInputDialog(this,"Please Enter The Clubber's Key ");
		//if pressed cancel close window
		if(key == null)
			return;

		ClubAbstractEntity clubber = search(key);
		if(clubber == null)
		{//if clubber not in system
			String message = String.format("Clubber with key %s does not exist",key);  
			JOptionPane.showMessageDialog(this,message,"",JOptionPane.INFORMATION_MESSAGE);
		}else clubber.setVisible(true);
			
	}

	
   /**
   * This method checks if a clubber with an identical id to the received clubber exists in the database.
   * The method uses {@link #search} and {@link Person#getId} to search with the key in the id text field of the clubber.
   * 
   * @param curr current clubber that is being added to database
   * @return true if a clubber with an identical  id exists in database otherwise false.
   */
	public static boolean isExists(ClubAbstractEntity curr)
	{
		
			String key = ((Person)curr).getId();
			ClubAbstractEntity clubber = search(key);
			if(search(key) == null || clubber == curr )
				return false;
			
				
			String message = String.format("Clubber with key %s already exists",key);
			JOptionPane.showMessageDialog(curr,message,"",JOptionPane.INFORMATION_MESSAGE);
			return true;

	}
	
	/**
	* Loads all data from  BKCustomers.dat using {@link ObjectInputStream} and {@link FileInputStream}. 
	* Creates the corresponding objects and places them in the clubbers ArrayList of {@link ClubAbstractEntity} objects.
	* Exception handling with try-catch {@link IOException} for file not found and loading exceptions.
	*/
	private void loadClubbersDBFromFile()
	{
			try
		{
			FileInputStream readData = new FileInputStream("BKCustomers.dat");
			ObjectInputStream readStream = new ObjectInputStream(readData);
		
			clubbers = (ArrayList<ClubAbstractEntity>) readStream.readObject();
			readStream.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	* This method writes all objects data in clubbers {@link ArrayList} 
	* in the BKCustomers.dat file using {@link java.io.ObjectOutputStream} ,{@link FileOutputStream}.
	* Non validated clubbers are removes from the list if empty, and set to validated form if valid 
	* using {@link ClubAbstractEntity#rollBack} method in package.
	* Exception handling with try-catch {@link IOException} 
	*/
	private void writeClubbersDBtoFile()
	{
		//removes non validated clubbers from arraylist before committing to file
		clubbers.removeIf(clubber->clubber.match(""));
		/*use rollBack to return accurate values to text-fields and remove error symbols
		when closing the app before closing the clubbers frame.
		rollBack is a protected method in package and therefore can be used
		*/
		clubbers.forEach(clubber->clubber.rollBack());
		try{
			
			FileOutputStream writeData = new FileOutputStream("BKCustomers.dat");
			ObjectOutputStream writeStream = new ObjectOutputStream(writeData);
		
			writeStream.writeObject(clubbers);
			writeStream.flush();
			writeStream.close();
		
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	* This is a inner class for buttons search and create event handling.
	* The implementation of this handler is generic and correct for any type of customer,
    * if "Search" is pressed {@link #manipulateDB} is invoked.  
    * if "Create" is pressed it creates new clubber corresponding the selected index on the {@link JComboBox},
    * and invokes the corresponding constructor {@link Person} , {@link Soldier} or {@link Student}
    */
    private class ButtonHandler implements ActionListener
    {
    	@Override
    	public void actionPerformed(ActionEvent event)
    	{
    		switch(event.getActionCommand())
    		{
    		case "Search" :
    			manipulateDB();
    			break;
    		case "Create" :
    			switch(clubberType.getSelectedIndex())
    			{
    				case 0:
						clubbers.add(new Person());
						break;
    				case 1:
						clubbers.add(new Soldier());
						break;
    				case 2:
						clubbers.add(new Student());
						break;
    			}
    			clubbers.get(clubbers.size() - 1).setVisible(true);
    			break;
    		}
    	}
     }
      
     /**
     * Main function creates a new NightClubMgmtApp to start the application
     */
    public static void main(String[] args)
	{
		NightClubMgmtApp application = new NightClubMgmtApp();
	}
   	
}