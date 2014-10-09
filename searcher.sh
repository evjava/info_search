libs=`find libs/ -name "*jar" | sed ':a;N;$!ba;s/\n/:/g'`
java -cp $libs:./out/production/info_search Searcher $1
