echo ">>>>> TEST GET METHOD OF LogFileInfoServlet SERVLET <<<<<"
content=$(curl -i -H "Accept: application/json" -H "Content-Type: application/json" 'http://localhost:13111/LogFileInfoServlet')
echo "Result response data after GET request:"
echo "--------------------------------------------"
echo $content 
echo "--------------------------------------------"
echo "Check OK if content contains string : [wiperdog.log]"
if [[ $content =~ .*'"wiperdog.log"'.* ]]
then
	echo "[MESSAGE TEST] GET IS OK" 
	echo ">>>>> TEST POST METHOD OF LogFileInfoServlet SERVLET <<<<<"
	contentPost=$(curl -X POST -H "Accept: application/json" -H "Content-type: application/json" -d '{"log":"wiperdog.log"}' http://localhost:13111/LogFileInfoServlet -v)
	echo "Result response data after POST request:"
	echo "--------------------------------------------"
	echo $contentPost 
	echo "--------------------------------------------"
	echo "Check OK if content contains string : [logContent]"

	if [[ $contentPost =~ .*'{"logContent":'.* ]]
	then
		echo "[MESSAGE TEST] POST IS OK"
	else
		echo "[MESSAGE TEST] POST IS FAILURE"
	fi
else
	echo "[MESSAGE TEST] GET IS FAILURE, SO CANNOT TEST POST METHOD"
fi