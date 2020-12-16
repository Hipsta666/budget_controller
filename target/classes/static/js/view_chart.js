function viewChart(currTag, tag2, tag3, e1, e2, e3){
  document.getElementById(currTag).style.backgroundColor = "#94b1da";
  document.getElementById(tag2).style.backgroundColor = "#2b2b2b";
  document.getElementById(tag3).style.backgroundColor = "#2b2b2b";
  document.getElementById(e1).style.display = 'block';
  document.getElementById(e2).style.display = 'none';
  document.getElementById(e3).style.display = 'none';
}

$(document).ready(function() {
            google.charts.load('current', {
                packages : [ 'corechart']
            });
            google.charts.setOnLoadCallback(drawDayChart);
            google.charts.setOnLoadCallback(drawWeekChart);
            google.charts.setOnLoadCallback(drawMonthChart);
        });


        function drawDayChart() {
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Category');
            data.addColumn('number', 'Sums');
            Object.keys(dayData).forEach(function(key) {
                data.addRow([ key, dayData[key] ]);
            });
            var options = {
                is3D: true,
                'width':700,
                'height':400,
            };
            var dChart = new google.visualization.PieChart(document
                    .getElementById('dayChart'));

            dChart.draw(data, options);
        }

        function drawWeekChart() {
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Category');
            data.addColumn('number', 'Sums');
            Object.keys(weekData).forEach(function(key) {
                data.addRow([ key, weekData[key] ]);
            });
            var options = {
                is3D: true,
                'width':700,
                'height':400,
            };
            var wChart = new google.visualization.PieChart(document
                    .getElementById('weekChart'));
            wChart.draw(data, options);


        }

        function drawMonthChart() {
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Category');
            data.addColumn('number', 'Sums');
            Object.keys(monthData).forEach(function(key) {
                data.addRow([ key, monthData[key] ]);
            });
            var options = {
                is3D: true,
                'width':700,
                'height':400,
            };
            var mChart = new google.visualization.PieChart(document
                    .getElementById('monthChart'));

            mChart.draw(data, options);
        }