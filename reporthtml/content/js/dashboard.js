/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
var showControllersOnly = false;
var seriesFilter = "";
var filtersOnlySampleSeries = true;

/*
 * Add header in statistics table to group metrics by category
 * format
 *
 */
function summaryTableHeader(header) {
    var newRow = header.insertRow(-1);
    newRow.className = "tablesorter-no-sort";
    var cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Requests";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 3;
    cell.innerHTML = "Executions";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 7;
    cell.innerHTML = "Response Times (ms)";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Throughput";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 2;
    cell.innerHTML = "Network (KB/sec)";
    newRow.appendChild(cell);
}

/*
 * Populates the table identified by id parameter with the specified data and
 * format
 *
 */
function createTable(table, info, formatter, defaultSorts, seriesIndex, headerCreator) {
    var tableRef = table[0];

    // Create header and populate it with data.titles array
    var header = tableRef.createTHead();

    // Call callback is available
    if(headerCreator) {
        headerCreator(header);
    }

    var newRow = header.insertRow(-1);
    for (var index = 0; index < info.titles.length; index++) {
        var cell = document.createElement('th');
        cell.innerHTML = info.titles[index];
        newRow.appendChild(cell);
    }

    var tBody;

    // Create overall body if defined
    if(info.overall){
        tBody = document.createElement('tbody');
        tBody.className = "tablesorter-no-sort";
        tableRef.appendChild(tBody);
        var newRow = tBody.insertRow(-1);
        var data = info.overall.data;
        for(var index=0;index < data.length; index++){
            var cell = newRow.insertCell(-1);
            cell.innerHTML = formatter ? formatter(index, data[index]): data[index];
        }
    }

    // Create regular body
    tBody = document.createElement('tbody');
    tableRef.appendChild(tBody);

    var regexp;
    if(seriesFilter) {
        regexp = new RegExp(seriesFilter, 'i');
    }
    // Populate body with data.items array
    for(var index=0; index < info.items.length; index++){
        var item = info.items[index];
        if((!regexp || filtersOnlySampleSeries && !info.supportsControllersDiscrimination || regexp.test(item.data[seriesIndex]))
                &&
                (!showControllersOnly || !info.supportsControllersDiscrimination || item.isController)){
            if(item.data.length > 0) {
                var newRow = tBody.insertRow(-1);
                for(var col=0; col < item.data.length; col++){
                    var cell = newRow.insertCell(-1);
                    cell.innerHTML = formatter ? formatter(col, item.data[col]) : item.data[col];
                }
            }
        }
    }

    // Add support of columns sort
    table.tablesorter({sortList : defaultSorts});
}

$(document).ready(function() {

    // Customize table sorter default options
    $.extend( $.tablesorter.defaults, {
        theme: 'blue',
        cssInfoBlock: "tablesorter-no-sort",
        widthFixed: true,
        widgets: ['zebra']
    });

    var data = {"OkPercent": 61.325401989032436, "KoPercent": 38.674598010967564};
    var dataset = [
        {
            "label" : "FAIL",
            "data" : data.KoPercent,
            "color" : "#FF6347"
        },
        {
            "label" : "PASS",
            "data" : data.OkPercent,
            "color" : "#9ACD32"
        }];
    $.plot($("#flot-requests-summary"), dataset, {
        series : {
            pie : {
                show : true,
                radius : 1,
                label : {
                    show : true,
                    radius : 3 / 4,
                    formatter : function(label, series) {
                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'
                            + label
                            + '<br/>'
                            + Math.round10(series.percent, -2)
                            + '%</div>';
                    },
                    background : {
                        opacity : 0.5,
                        color : '#000'
                    }
                }
            }
        },
        legend : {
            show : true
        }
    });

    // Creates APDEX table
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.6132540198903244, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [1.0, 500, 1500, "Запрос 2: Создание категории (POST)  "], "isController": false}, {"data": [0.09740259740259741, 500, 1500, "Запрос 5: Получение списка операций (GET)  "], "isController": false}, {"data": [0.0, 500, 1500, "Запрос 7: Пауза между итерациями  "], "isController": false}, {"data": [0.9850649350649351, 500, 1500, "Запрос 6: Получение аналитики (GET)  "], "isController": false}, {"data": [1.0, 500, 1500, "Запрос 1: Создание счёта (POST) "], "isController": false}, {"data": [0.2025974025974026, 500, 1500, "Запрос 4: Получение списка счетов (GET)  "], "isController": false}, {"data": [1.0, 500, 1500, "3 - Создать операцию"], "isController": false}]}, function(index, item){
        switch(index){
            case 0:
                item = item.toFixed(3);
                break;
            case 1:
            case 2:
                item = formatDuration(item);
                break;
        }
        return item;
    }, [[0, 0]], 3);

    // Create statistics table
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 10759, 4161, 38.674598010967564, 1.3306998791709301, 0, 110, 1.0, 2.0, 3.0, 21.799999999999272, 131.37554185237192, 103.52790730508579, 27.847789566670738], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["Запрос 2: Создание категории (POST)  ", 1540, 0, 0.0, 0.5863636363636378, 0, 5, 1.0, 1.0, 1.0, 2.0, 18.811687677123032, 6.820727918407358, 4.5559556093032345], "isController": false}, {"data": ["Запрос 5: Получение списка операций (GET)  ", 1540, 1390, 90.25974025974025, 2.5233766233766244, 0, 78, 1.0, 4.0, 6.0, 31.589999999999918, 18.808700856162293, 25.66693027345897, 2.9755952526350504], "isController": false}, {"data": ["Запрос 7: Пауза между итерациями  ", 1520, 1520, 100.0, 0.6947368421052635, 0, 6, 1.0, 1.0, 2.0, 3.0, 18.80443388756928, 6.464024148851941, 2.7178283353127477], "isController": false}, {"data": ["Запрос 6: Получение аналитики (GET)  ", 1540, 23, 1.4935064935064934, 1.172077922077922, 0, 108, 1.0, 1.0, 1.0, 30.589999999999918, 18.808700856162293, 5.892377533525899, 3.544999282460276], "isController": false}, {"data": ["Запрос 1: Создание счёта (POST) ", 1539, 0, 0.0, 0.6978557504873296, 0, 9, 1.0, 1.0, 1.0, 4.0, 18.879497528122968, 6.7630040934713005, 4.674410801581265], "isController": false}, {"data": ["Запрос 4: Получение списка счетов (GET)  ", 1540, 1228, 79.74025974025975, 3.0305194805194793, 0, 110, 1.0, 4.0, 8.0, 37.0, 18.81007927104836, 43.807793854050885, 2.939074886101306], "isController": false}, {"data": ["3 - Создать операцию", 1540, 0, 0.0, 0.6012987012987021, 0, 7, 1.0, 1.0, 1.0, 2.0, 18.813066529844367, 8.254254413893571, 6.503735890200102], "isController": false}]}, function(index, item){
        switch(index){
            // Errors pct
            case 3:
                item = item.toFixed(2) + '%';
                break;
            // Mean
            case 4:
            // Mean
            case 7:
            // Median
            case 8:
            // Percentile 1
            case 9:
            // Percentile 2
            case 10:
            // Percentile 3
            case 11:
            // Throughput
            case 12:
            // Kbytes/s
            case 13:
            // Sent Kbytes/s
                item = item.toFixed(2);
                break;
        }
        return item;
    }, [[0, 0]], 0, summaryTableHeader);

    // Create error table
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["500/Server Error", 2641, 63.470319634703195, 24.546890974997677], "isController": false}, {"data": ["404/Not Found", 1520, 36.529680365296805, 14.127707035969886], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 10759, 4161, "500/Server Error", 2641, "404/Not Found", 1520, "", "", "", "", "", ""], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": [], "isController": false}, {"data": ["Запрос 5: Получение списка операций (GET)  ", 1540, 1390, "500/Server Error", 1390, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Запрос 7: Пауза между итерациями  ", 1520, 1520, "404/Not Found", 1520, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Запрос 6: Получение аналитики (GET)  ", 1540, 23, "500/Server Error", 23, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": ["Запрос 4: Получение списка счетов (GET)  ", 1540, 1228, "500/Server Error", 1228, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
