declare -r ama="$(ps -ef | grep deploy.sh | awk -F" " '{print $2}')"
for rootProcess in $ama; do
    for child in "$(pgrep -P $rootProcess)"; do
    	kill $child;
    done;
done;