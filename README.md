# Java-UDP-client-server-implementation

A simple UDP server and client implementation.  

The server will accept UDP packets with a payload consisting of ASCII
characters starting with either get, put, swap or remove. The colon character is used as a delimiter, 
so a get command should be formatted as

Command       Found?  Payload

get           
              yes      success

              no      no match

put           
              yes     updated:key

swap          
              yes     success

              no      no match

remove        
              yes     success

              no      no match


If the input packet is not a well-formed get, put, swap or remove, the server replies with

error:unrecognizable input:copy of the input packet’s payload here

The server takes an optional command line argument. If present, it is interpreted as the 

port number that the server is to use. If no port number is specified, the server uses port 31357.

The client program takes 4-5 arguments. The first two arguments are the name of 

the host that the server is running on and the port number on which it’s listening. The third 

argument is either get, put, swap or remove and the remaining arguments are the argument 

strings for its respective operation. The client sends an appropriately formatted get, put, 

swap or remove packet to the server (based on the command line arguments) and prints the 

payload of the packet returned by the server.
