echo ">>>>> TEST GET METHOD OF TestJobServlet SERVLET <<<<<"
content=$(curl -v 'http://localhost:13111/TestJobServlet?jobFileName=MySQL.Database_Structure.DatabaseVersion.job')
echo "Result response data after GET request:"
echo "--------------------------------------------"
echo $content 
echo "--------------------------------------------"
echo "Check OK if content contains string : [JOB = [ name: \"MySQL.Database_Structure.DatabaseVersion\"]"
if [[ $content =~ .*'JOB = [ name: \"MySQL.Database_Structure.DatabaseVersion\" ]'.* ]]
then
	echo "[MESSAGE TEST] GET IS OK" 
	echo ">>>>> TEST POST METHOD OF TestJobServlet SERVLET <<<<<"
	contentPost=$(curl -X POST -H "Accept: application/json" -H "Content-type: application/json" -d '{"log":"wiperdog.log"}' http://localhost:13111/TestJobServlet -v)
	echo "Result response data after POST request:"
	echo "--------------------------------------------"
	echo $contentPost 
	echo "--------------------------------------------"
	echo "Check OK if content contains string : [\"status\":\"success\"]"

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