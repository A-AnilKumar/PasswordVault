# PasswordVault

Java Console application to securely store the user's password and credentials for their social and personal accounts in one place. 
The user credentials are stored in a database using the **JDBC API**, security question is hashed using the password hashing function **BCrypt** to ensure proper user authentication.
	
# To Run 
	
# Requirements : 

**Java** installed on your machine

**Maven dependencies** 

**MySQL JDBC Connector**

**Bcrypt** hashing algorithm dependency
	
# Make changes according to your details in the **config.properties** file.
username, password, and database url


Also, provide the path for the “path of config.properties file” in the  **ConfigPropertyReader.java** file in utility package.


Make sure that you have already created a database named **“passwordvault”** before running the application, and a table named **“accountstable”** with the following list : 
  **Username, password, mailId , securityquestion and securityans**.
  
  The above list can be found in the code. 
