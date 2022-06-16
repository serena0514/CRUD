

Purpse:

    this is a simple single-threaded key-value store in both TCP and UDP connections. 
    you can do PUT, GET, and DELETE to interact with the server
    
    You can continuously exchange and put request to interact with the server

How to run:
TCP
    -compile for TCP:
        javac KeyDataServerTCP.java
        javac KeyDataClientTCP.java
    -run
        java KeyDataServerTCP (port) --optional
        java KeyDataClientTCP (port) --optional
    -quit:
        type "bye" in the client console

 UDP:
    -complie:
        javac UDPServer.java
        javac UDPClient.java
    -run 
        javac UDPServer (port) --optional
        javac UDPClient (port) --optional
    -quit
        type bye in the client console
        
