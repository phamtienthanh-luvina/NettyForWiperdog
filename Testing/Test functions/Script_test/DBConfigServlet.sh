echo ">>>>> TEST GET METHOD OF DBCONFIGURATION SERVLET <<<<<"
content=$(curl -i -H "Accept: application/json" -H "Content-Type: application/json" 'http://localhost:13111/DBConfigServlet')
echo "Result response data after GET request:"
echo "--------------------------------------------"
echo $content 
echo "--------------------------------------------"
echo "Check OK if content contains string : [jdbc:mysql://localhost:3306/information_schema]"
if [[ $content =~ .*'jdbc:mysql://localhost:3306/information_schema'.* ]]
then
	echo "[MESSAGE TEST] GET IS OK"
	echo ">>>>> TEST POST METHOD OF DBCONFIGURATION SERVLET <<<<<"
	contentPost=$(curl -X POST -H "Accept: application/json" -H "Content-type: application/json" -d '{"@MYSQL":{"dbconnstr":"jdbc:mysql://localhost:3306/information_schema","user":"root","dbHostId":"localhost","dbSid":"information_schema"},"localhost-@MYSQL-information_schema":{"dbconnstr":"jdbc:mysql://localhost:3306/information_schema","user":"root","dbHostId":"localhost","dbSid":"information_schema"},"@PGSQL":{"dbconnstr":"jdbc:postgresql://localhost:5432/postgres","user":"postgres","dbHostId":"localhost","dbSid":"postgres"},"localhost-@PGSQL-postgres":{"dbconnstr":"jdbc:postgresql://localhost:5432/postgres","user":"postgres","dbHostId":"localhost","dbSid":"postgres"},"@MSSQL-MSSQLSERVER2008":{"dbconnstr":"jdbc:sqlserver://localhost:1433","user":"sa","dbHostId":"localhost","dbSid":"MSSQLSERVER2008"},"@MSSQL":{"dbconnstr":"jdbc:sqlserver://localhost:1433","user":"sa","dbHostId":"localhost","dbSid":"MSSQLSERVER2008"},"curlHostID-@PGSQL-curlSid":{"dbconnstr":"jdbc:postgresql://curlHostName:5432","user":"curlUserName","dbHostId":"curlHostID","dbSid":"curlSid"}}' http://localhost:13111/DBConfigServlet -v)
	echo "Result response data after POST request:"
	echo "--------------------------------------------"
	echo $contentPost 
	echo "--------------------------------------------"
	echo "Check OK if content contains string : [status]"

	if [[ $contentPost =~ .*'status'.* ]]
	then
		echo "[MESSAGE TEST] POST IS OK"
		echo "*NOTE: CHECK CONTENT OF FILE var/conf/default.params FOR SURE"
	fi
else
	echo "[MESSAGE TEST] GET IS FAILURE, SO CANNOT TEST POST METHOD"
fi