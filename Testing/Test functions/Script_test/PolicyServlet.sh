echo ">>>>> TEST GET METHOD OF POLICY SERVLET <<<<<"
content=$(curl -i -H "Accept: application/json" -H "Content-Type: application/json" 'http://localhost:13111/policyServlet?job_name=Postgres.Database_Area.Tablespace_Free&type=localhost-@PGSQL-postgres')
echo "Result response data after GET request:"
echo "--------------------------------------------"
echo $content 
echo "--------------------------------------------"
echo "Check OK if content contains [POLICY] string"
if [[ $content =~ .*'"POLICY":'.* ]]
then
	echo "[MESSAGE TEST] GET IS OK"
	echo ">>>>> TEST POST METHOD OF POLICY SERVLET <<<<<"
	contentPost=$(curl -X POST -H "Accept: application/json" -H "Content-type: application/json" -d '{"action":"WRITE2FILE","jobName":"Postgres.Database_Area.Tablespace_Free","instanceName":"localhost-@PGSQL-postgres","policyStr":"POLICY = {resultData->\n def listMess = []\n def ret = ['jobName' : 'Postgres.Database_Area.Tablespace_Free', 'istIid' : 'localhost-@PGSQL-postgres']\n resultData.each{data->\n  if((data.UsedPct < 1)){\n   listMess.add([level: 2, message: \"\"\"Show is ok\"\"\"])\n  }\n }\n ret['message'] = listMess\n return ret\n}"}' 'http://localhost:13111/policyServlet' -v)
	echo "Result response data after POST request:"
	echo "--------------------------------------------"
	echo $contentPost 
	echo "--------------------------------------------"
	echo "Check OK if content contains [status] string"
	if [[ $contentPost =~ .*'status'.* ]]
	then
		echo "[MESSAGE TEST] POST IS OK"
		echo "*NOTE: CHECK IN FOLDER VAR/JOB/POLICY: Postgres.Database_Area.Tablespace_Free.localhost-@PGSQL-postgres.policy WAS CREATED !!!"
	fi
else
	echo "[MESSAGE TEST] GET IS FAILURE, SO CANNOT TEST POST METHOD"
fi