<html>
    <head>
        <title>Index</title>
        <link rel="stylesheet" href="/in.css">
    </head>

    <body class="intro">
        <div class="w3-container w3-teal">
          <p>The w3-container class can be used to display footers.</p>
        </div>
        <#list categories as item>
            ${item.id}
            ${item.name}
            ${item.parentId}
            <br>
        </#list>
    </body>
</html>
