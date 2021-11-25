{\rtf1\ansi\ansicpg1252\cocoartf1404\cocoasubrtf470
{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 Client.java: \
\
	-> The client class that sends the messages\
	-> Sends 10 messages; alternates between read and write\
	-> After the 10 messages; sends an invalid messages\
	-> After sending messages to intermediate, waits for a response\
	-> While it sends the first messages, must be started lastly\
	-> In order to run this class; run the main method\
\
Intermediate.java:\
	\
	-> The intermediate class that transfers messages and responses between server and client\
	-> Must be started before client but after server\
	-> Runs infinitely to keep transferring messages and responses\
	-> In order to run this class; run the main method\
\
Server.java: \
\
	-> The server class that receives messages from intermediate and responds\
	-> Sends a response that changes depending on messages (0301 for read; 0400 for write; 	exits for invalid)\
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0
\cf0 	-> Runs infinitely to keep receiving messages and sending responses (until invalid message)\
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0
\cf0 	-> Must be started first in order to receive a messages\
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0
\cf0 	-> In order to run this class; run the main method\
\
Instructions: \
\
	1: Run Server(main)\
	2: Run Intermediate(main)\
	3: Run Client(main)\
}