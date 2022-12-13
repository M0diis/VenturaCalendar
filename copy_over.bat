ssh ubuntu@130.61.236.156 "cd /home/ubuntu/Desktop/Servers/TestServer/plugins; rm VenturaCalendar-*" 
scp build/libs/VenturaCalendar* ubuntu@130.61.236.156:/home/ubuntu/Desktop/Servers/TestServer/plugins
ssh ubuntu@130.61.236.156 "screen -S server -X eval 'stuff \"plugman reload VenturaCalendar\015\"'"