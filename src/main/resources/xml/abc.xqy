declare variable $username external;
declare variable $password external;
for $x in doc("src/main/resources/xml/users.xml")/users/user
where $x/username=$username
and $x/password=$password
return $x/group/text()

