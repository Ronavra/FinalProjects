import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
* This class represents ordinary person clubber.
* The Person is represented by 4 fields: id, name, last-name and phone-number.
* The class extends {@link ClubAbstractEntity} which is abstract class and is a JFrame and with gui elements and methods.
* The class defines the inherited methods match, validateData, commit and rollBack.
*/
public class Person extends ClubAbstractEntity implements Serializable
{
	private String [] fieldString;
	private JTextField [] textField;
	
	
	/**
	* Parameterless constructor - creates a Persons fields rows using {@link ClubAbstractEntity#createRow} method
	* adds them to the center panel using  {@link ClubAbstractEntity#addToCenter} method,
	* and initializes the fields instance variables to empty values.
	* disables the cancel button until the first commit.
	* sets window size and title.
	*/
		public Person()
	{
		this("", "", "", "");
	}
	
	
	/** 
	* Constructor with four string parameters corresponding to the fields.
	* Creates and initializes the instance variables.
	* creates and initializes the rows by using {@link ClubAbstractEntity#createRow} method and 
	* adds them the center panel using {@link ClubAbstractEntity#addToCenter} method.
	* sets window size and title.
	*
	* @param id person id
	* @param name person name
	* @param surname person last name
	* @param tel person phone number
	*/
	public Person(String id, String name, String surname, String tel)
	{
		String [] fieldName = {"ID", "Name", "Surname" ,"Tel"};
		fieldString = new String[]{id, name, surname, tel};
	    textField = new JTextField[4];
	    
		for(int i = 0; i  < 4; i++)
		{
			textField[i] = new JTextField(fieldString[i],30);
			addToCenter(createRow(fieldName[i],textField[i]));
		}
			
		setTitle("Persons Clubber's Data");
		setSize(450, 220);
	}
	
	
	
	/**
	* This method overrides {@link ClubAbstractEntity#match} method and indicates whether 
	* the id key matches the person id value.
	*
	* @param key key parameter
	* @return true if the key equals the id field otherwise return false
	*/
	@Override
    public  boolean match(String key)
    {
    	return  fieldString[0].equals(key);
    }
    
	/**
	* This method overrides {@link ClubAbstractEntity#validateData} method and indicates whether 
	* The data in the fields is valid or not.
	* The validation uses regular expressions patterns {@link java.util.regex.Pattern} to determine validity.
	* Id field - "\\d-\\d{7}\\|[1-9]"
	* Name field -  "[A-Z][a-z]+"
	* Last-name field - "([A-Z][a-z]*['-]?)+"
	* Phone-number field -  "\\+\\([1-9]\\d{0,2}\\)[1-9]\\d{0,2}-[1-9]\\d{6}"}
	* The fields are checked consecutively until either the first error or all fields are found valid. 
	* Invokes {@link ClubAbstractEntity#setError} that add or remove '*' in the corresponding field to indicate valid or error field.
	*
	* @return true if fields are found valid otherwise returns false. 
	*/
    @Override
    protected  boolean validateData()
    {
    	String [] reg = {"\\d-\\d{7}\\|[1-9]", "[A-Z][a-z]+", "([A-Z][a-z]*['-]?)+",
    					"\\+\\([1-9]\\d{0,2}\\)[1-9]\\d{0,2}-[1-9]\\d{6}"};
    					
    	for(int i = 0; i < 4; i++)			
			if(textField[i].getText().matches(reg[i]))
			  	setError(true, textField[i]);
			else
			{
				setError(false, textField[i]);
				return false;
			}
    	
			return true;
    }
    
    /**
	* This method overrides {@link ClubAbstractEntity#commit} method. 
	* Stores the person information from the textFields to the corresponding person value fields.
	*/
    @Override
    protected  void commit()
    {
    	for(int i = 0; i < 4; i++)
    		fieldString[i] = textField[i].getText();
    }

    /**
	* This method overrides {@link ClubAbstractEntity#rollBack}  method. 
	* The method returns the values from the persons value fields to the corresponding textFields,
	* and clears all erroneous signs from the fields using {@link ClubAbstractEntity#setError} method.
	*/
    @Override
    protected  void rollBack()
    {
    	for(int i = 0; i < 4; i++)
    	{
    		textField[i].setText(fieldString[i]);
    		setError(true, textField[i]);
    	}
    }
	/**
	* This method returns the id inserted by user in the id textField
	*@return the id of the person
	*/
	public String getId()
	{
		return textField[0].getText();
	}

}