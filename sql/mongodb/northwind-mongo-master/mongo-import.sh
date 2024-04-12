for f in *.csv
do
    echo [$f]
    filename=$(basename "$f")
    extension="${filename##*.}"
    filename="${filename%.*}"
    mongoimport -d Northwind -c "$filename" --type csv --file "$f" --headerline
done
