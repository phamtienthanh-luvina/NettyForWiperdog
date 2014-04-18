echo ">>>>> TEST GET METHOD OF TestJobServlet SERVLET <<<<<"
content=$(curl -v 'http://localhost:13111/TestJobServlet?jobFileName=MySQL.Database_Structure.DatabaseVersion.job')
if [[ $content =~ .*'JOB = [ name: \"MySQL.Database_Structure.DatabaseVersion\" ]'.* ]]
then
	echo "[MESSAGE TEST] GET IS OK" 
	echo ">>>>> TEST POST METHOD OF TestJobServlet SERVLET <<<<<"
	contentPost=$(curl -X POST -H "Accept: application/json" -H "Content-type: application/json" -d '{"log":"wiperdog.log"}' http://localhost:13111/TestJobServlet -v)
	
	#'status' is response data from serlvet ,regadless it is 'failed' or 'sucess' ,it show that servlet is work fine
	if [[ $contentPost =~ .*'"status":"success"'.*  ||   $contentPost =~ .*'"status":"failed"'.* ]]
	then
		echo "[MESSAGE TEST] POST IS OK"
	else
		echo "[MESSAGE TEST] POST IS FAILURE"
	fi
else
	echo "[MESSAGE TEST] GET IS FAILURE, SO CANNOT TEST POST METHOD"
fi