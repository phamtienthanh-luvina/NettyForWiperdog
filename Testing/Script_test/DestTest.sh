echo ">>>>> TEST GET METHOD OF DESTEST SERVLET <<<<<"
content=$(curl -i -H "Accept: application/json" -H "Content-Type: application/json" http://localhost:13111/spool/tfm_test)

if [[ $content =~ .*'Put to insert data in MongoDB!!!'.* ]]
then
	echo "[MESSAGE TEST] GET IS OK"
	echo ">>>>> TEST PUT METHOD OF DESTEST SERVLET <<<<<"
	curl -X PUT -H "Accept: application/json" -H "Content-type: application/json" -d '{"sourceJob":"abc", "istIid": "def"}' http://localhost:13111/spool/tfm_test -v
	echo "[MESSAGE TEST] PLEASE CHECK DATA IN MONGODB TO VIEW RESULT:"
	echo " - If collection abc.def has new row: PUT IS OK"
	echo " - Else: PUT IS FAILURE"
else
	echo "[MESSAGE TEST] GET IS FAILURE, SO CANNOT TEST PUT METHOD"
fi