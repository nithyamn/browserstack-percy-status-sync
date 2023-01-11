BUILD_ID=$(npx percy exec -- mvn test -P sample | grep builds | awk -F "/" '{print $NF}')
#echo $BUILD_ID
npx percy build:wait --build $BUILD_ID
mvn test -P mark-test-status -Dbuildid=$BUILD_ID
