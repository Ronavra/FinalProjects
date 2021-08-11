import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
* This class extends class {@link Person} and represents a student clubber.
* In addition to the inherited characteristics of the Person the student has student id field.
* This class implements all abstract methods from {@link ClubAbstractEntity}
*/
public class Student extends Person
{
	private JTextField studentIdField;
	private String studentId;
	
    /**
	* Parameterless constructor - creates a Students fields rows using {@link ClubAbstractEntity#createRow} method
	* adds them to the center panel using  {@link ClubAbstractEntity#addToCenter} method,
	* and initializes the fields instance variables to empty values.
	* disables the cancel button until the first commit.
	* sets window size and title.
	*/
	public Student()
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
	* @param studentId student id number
	*/	
	public Student(String id, String name, String surname, String tel, String studentId)
	{
		super(id, name, surname, tel);
		
		this.studentId = studentId;
		studentIdField = new JTextField(studentId, 30);
		addToCenter(createRow("Student ID",studentIdField));
	
		setTitle("Student Clubber's Data");
		setSize(450, 250);
	}
	
	/**
	* This method overrides {@link ClubAbstractEntity#match} method and indicates whether or not 
	* the key equals to the person or student id field of the current student.
	*In-order to match the studentId the key must match the digits (after the / character) 
	*
	* @param key key parameter
	* @return true if the key equals the person or student id otherwise returns false
	*/	
	@Override
    public  boolean match(String key)
    {//if before first commit studentId can be empty field
    	return super.match(key) || (!studentId.equals("") && studentId.substring(4).equals(key));
    }


	/**
	* This method is a collaborative override of {@link Person#validateData} the method indicates whether 
	* the data in the fields is valid or not.
	* In addition to the regular expressions used to validate the {@link Person} fields,
	* this method checks the students added field studentId with the regular expresion - "[A-Z]{3}/[1-9]\\d{4}"
	* The fields are checked consecutively until either the first error or all fields are found valid. 
	* Invokes {@link ClubAbstractEntity#setError} that add or remove '*' in the corresponding field to indicate valid or error field.
	*
	* @return true if fields are found valid otherwise returns false. 
	*/ 
    @Override
    protected  boolean validateData()
    {
    	if(!super.validateData())
    		return false;
    	if(studentIdField.getText().matches("[A-Z]{3}/[1-9]\\d{4}"))
    		setError(true, studentIdField);
    	else
    	{
    		setError(false, studentIdField);
    		return false;
    	}
    	
		return true;
    }

    /**
	* This method is a collaborative override of {@link Person#commit} method.
	* The method invokes commit function from {@link Person}
	* that stores the Person information from the textFields to the corresponding person value fields
	* and then stores the student id in the corresponding value field.
	*/ 
    @Override
    protected  void commit()
    {
    	super.commit();
    	studentId = studentIdField.getText();
    }

    /**
	* This method is a collaborative override of {@link Person#rollBack} method.
	* The method invokes the commit function from {@link Person}
	* returns the values from the persons value fields to the corresponding textFields,
	* and clears all erroneous signs from the fields using {@link ClubAbstractEntity#setError} method
	* and then does the same with the added studentId field.
	*/    
    @Override
    protected  void rollBack()
    {
    	super.rollBack();
    	studentIdField.setText(studentId);
    	setError(true, studentIdField);
    }

}