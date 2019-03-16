<!DOCTYPE html>
<html>

<head>
    <script>

    </script>
    <meta charset="ISO-8859-1">
    <title>Insert title here</title>
</head>

<body>
    <table border="1">

        <caption>PRODUCT TABLE</caption>
        <form name="" method="" action="success.html" entctype="multipart/form-data">
            <tr style="background-color:AQUA">
                <th>Name</th>
                <th>Description</th>
                <th>Price</th>
                <th>Qty</th>
            </tr>

            <tr>
                <td>Barbie</td>
                <td>Beautiful</td>
                <td>20</td>
                <td><input type="number" id="barbie" required loc></td>
            </tr>
            <td>Calculator</td>
            <td>Calculator with Latest Features</td>
            <td>30</td>
            <td> <input type="number" required id="calc"></td>
            </tr>
            <td>MobilePhone</td>
            <td>Camera,Java Games,GPRS</td>
            <td>40</td>
            <td> <input type="number" required id="mob"></td>
            </tr>
            <td>LG DVD</td>
            <td>5 disc changer</td>
            <td>50</td>
            <td> <input type="number" required id="lg"></td>
            </tr>
            <td><input type="button" value="Order" onclick=compute()>


                <script>
                    function compute() {
                        var barbie = document.getElementById("barbie").value;

                        var calc = document.getElementById("calc").value;
                        var mobPhone = document.getElementById("mob").nodeValue;
                        var lg = document.getElementById("lg").nodeValue;


                        var myWindow = window.open();

                        myWindow.document.write("barbie qty" + barbie);
                        myWindow.document.write("<table border='1'>");
                        myWindow.document.write("<tr><td>PRODUCT</td><td>Quantity</td><td>Price</td><td>Total</td></tr>");
                        myWindow.document.write("<tr><td>Barbie Doll</td><td id='barbie1'></td><td>20</td><td>barbie</td></tr>");
                        myWindow.document.write(" <tr><td>Calculator</td><td>calc</td><td>30</td><td>calc</td></tr>")
                        myWindow.document.write("<tr><td>MobilePhone</td><td>mobPhone</td><td>40</td><td>mobPhone</td></tr>")
                        myWindow.document.write(" <tr><td>LG DVD</td><td>lg</td><td>50</td><td>lg</td></tr>")
                        myWindow.document.write("</table>");
                        document.getElementById("barbie1").innerHTML = barbie;
                    }

                </script>


        </form>
    </table>

</body>

</html>
