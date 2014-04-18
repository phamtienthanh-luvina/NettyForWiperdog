echo ">>>>> TEST GET METHOD OF IMPORT INSTANCE SERVLET <<<<<"
content=$(curl -i -H "Accept: application/json" -H "Content-Type: application/json" 'http://localhost:13111/ImportInstanceServlet?action=getListJob')
if [[ $content =~ .*'"data"'.*'"status"'.* ]]
then
	echo "[MESSAGE TEST] GET IS OK"
	echo ">>>>> TEST POST METHOD OF IMPORT INSTANCE SERVLET <<<<<"
	contentPost=$(curl -X POST -H "Accept: application/json" -H "Content-type: application/json" -d '[{"INST_NAME":"inst_1","SCHEDULE":"10i","PARAMS":{"PARAM3":"val_par31","PARAM1":"val_par11","PARAM2":"val_par21"}}]' 'http://localhost:13111/ImportInstanceServlet?action=exportCSV&jobFileName=MySQL.Database_Area.Top30Database.job' -v)
	if [[ $contentPost =~ .*'status'.* ]]
	then
		echo "[MESSAGE TEST] POST IS OK"
		echo "*NOTE: CHECK IN FOLDER VAR/JOB: MySQL.Database_Area.Top30Database.csv WAS CREATED !!!"
	fi
else
	echo "[MESSAGE TEST] GET IS FAILURE, SO CANNOT TEST POST METHOD"
fi