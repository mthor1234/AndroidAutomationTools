# git log --diff-filter=A --follow --format=%aI -- AutoTags.kts

#git ls-tree -r --name-only HEAD | while read filename; do{
#  echo "$(git log -1 --format="%ad" -- $filename) $filename"
#done

cd C:\Users\mthornton\StudioProjects\KTLintTest\Rules\src\main\scripts
echo "$(git log -1 --format="%ad" -- C:\Users\mthornton\StudioProjects\KTLintTest\app\src\main\java\com\example\ktlinttest\MainActivity.kt)"