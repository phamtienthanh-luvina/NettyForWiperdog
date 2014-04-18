echo ">>>>> TEST GET METHOD OF JobDocInfoServlet SERVLET <<<<<"
content=$(curl -i -H "Accept: application/json" -H "Content-Type: application/json" 'http://localhost:13111/JobDocInfoServlet')
echo "Result response data after GET request:"
echo "--------------------------------------------"
echo $content 
echo "--------------------------------------------"
echo "Check OK if content contains string : [MySQL] & [SQL_Server] & [Postgres]"
if [[ $content =~ .*'"MySQL":'.*  &&  $content =~ .*'"SQL_Server":'.*   &&  $content =~ .*'"Postgres":'.* ]]
then
	echo "[MESSAGE TEST] GET IS OK"
	echo ">>>>> TEST POST METHOD OF DBCOMMONCONFIG SERVLET <<<<<"
	contentPost=$(curl -X POST -H "Accept: application/json" -H "Content-type: application/json" -d '{"job":"Postgres.Performance.Buffer_Cache_Usage.job"}' http://localhost:13111/JobDocInfoServlet -v)
	echo "Result response data after POST request:"
	echo "--------------------------------------------"
	echo $contentPost 
	echo "--------------------------------------------"
	echo "Check OK if content contains string : [SharedBuffersCnt:Total number of buffer cache was allocated]"
	if [[ $contentPost =~ .*'{"SharedBuffersCnt":"Total number of buffer cache was allocated\n"'.* ]]
	then
		echo "[MESSAGE TEST] POST IS OK"
	else
		echo "[MESSAGE TEST] POST IS FAILURE"
	fi
else
	echo "[MESSAGE TEST] GET IS FAILURE, SO CANNOT TEST POST METHOD"
fi