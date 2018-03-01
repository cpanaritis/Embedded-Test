# Question 1
For embedded software, some of java's mechanisms should not or are not used. Firstly, the garbage collector is used because it simplifies dynamic memory management and eliminates memory leaks that could be caused by not using the garbage collector. Built in exceptions in Java are used since unlike other low-level programs will not continue execution of the program until the exception is handled. Also, JIT should be avoided because it would take up additional memory space on the embedded system as well as it requires additional application launch time since it needs to start compiling first.    

# Note
In theory, if the code I made works and is not the most efficient method, it is trivial to make it more efficient (just need more time) since the logic is correct. 

# Assumptions
I have made some assumptions while coding due to some stuff being unclear. 
They are the following:
1. Besides the android UI, I will not be using any libraries. 
2. The header values and the data are all signed (hence using the byte datatype)
3. Le and Lc are both shorts since it is specified that the APDU cannot be greater than 250 bytes, so a short will suffice. In a normal APDU there is up to 65 540 bytes (header+data), so this solution does not work for that scenario. A possible method is to use char data type to represent Le and Lc since char is 16-but signed value.  
4. Since constructors cannot return values, returning the APDU as byte array will be done as a method.
5. Since it does not specify that the byte array is the only attribute of the APDU object, I have added a data array attribute in order to make encryption and decryption easier. 

