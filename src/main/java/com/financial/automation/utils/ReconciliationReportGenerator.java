package com.financial.automation.utils;

import com.financial.automation.models.PositionReport;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**

* Generates HTML evidence for expected-vs-actual position reconciliation.
  */
  public final class ReconciliationReportGenerator {

  private static final Path REPORT_PATH =
  Paths.get("target", "data-reports", "reconciliation-summary.html");

  private ReconciliationReportGenerator() {
  }

  /**

  * Generates the reconciliation HTML report.
  *
  * @param positions source PositionDetails count
  * @param expectedReports expected position report records
  * @param actualReports actual position report records
    */
    public static void generate(
    List<?> positions,
    List<PositionReport> expectedReports,
    List<PositionReport> actualReports) throws IOException {

    Set<String> expectedIds = extractIds(expectedReports);
    Set<String> actualIds = extractIds(actualReports);

    Set<String> missingIds = new HashSet<>(expectedIds);
    missingIds.removeAll(actualIds);

    Set<String> extraIds = new HashSet<>(actualIds);
    extraIds.removeAll(expectedIds);

    int duplicateRecords = countDuplicateRecords(actualReports);

    BigDecimal expectedQuantity =
    calculateTotalQuantity(expectedReports);

    BigDecimal actualQuantity =
    calculateTotalQuantity(actualReports);

    BigDecimal expectedTotalPrice =
    calculateTotalPrice(expectedReports);

    BigDecimal actualTotalPrice =
    calculateTotalPrice(actualReports);

    boolean quantitiesMatch =
    expectedQuantity.compareTo(actualQuantity) == 0;

    boolean pricesMatch =
    expectedTotalPrice.compareTo(actualTotalPrice) == 0;

    boolean overallPass =
    missingIds.isEmpty()
    && extraIds.isEmpty()
    && duplicateRecords == 0
    && quantitiesMatch
    && pricesMatch;

    String html = buildHtml(
    positions.size(),
    expectedReports,
    actualReports,
    missingIds,
    extraIds,
    duplicateRecords,
    expectedQuantity,
    actualQuantity,
    expectedTotalPrice,
    actualTotalPrice,
    overallPass
    );

    Files.createDirectories(REPORT_PATH.getParent());
    Files.writeString(
    REPORT_PATH,
    html,
    StandardCharsets.UTF_8
    );
    }

  private static Set<String> extractIds(
  List<PositionReport> reports) {
   Set<String> ids = new HashSet<>();
   for (PositionReport report : reports) {
       if (report.getPositionId() != null) {
           ids.add(report.getPositionId());
       }
   }
   return ids;
  }

  private static int countDuplicateRecords(
  List<PositionReport> reports) {
   Set<String> uniqueIds = new HashSet<>();
   int duplicates = 0;
   for (PositionReport report : reports) {
       String positionId = report.getPositionId();
       if (positionId == null) {
           continue;
       }
       if (!uniqueIds.add(positionId)) {
           duplicates++;
       }
   }
   return duplicates;
  }

  private static BigDecimal calculateTotalQuantity(
  List<PositionReport> reports) {
   BigDecimal total = BigDecimal.ZERO;
   for (PositionReport report : reports) {
       if (report.getQuantity() != null) {
           total = total.add(report.getQuantity());
       }
   }
   return total;
  }

  private static BigDecimal calculateTotalPrice(
  List<PositionReport> reports) {
   BigDecimal total = BigDecimal.ZERO;
   for (PositionReport report : reports) {
       if (report.getTotalPrice() != null) {
           total = total.add(report.getTotalPrice());
       }
   }
   return total;
  }

  private static String buildHtml(
  int sourcePositionCount,
  List<PositionReport> expectedReports,
  List<PositionReport> actualReports,
  Set<String> missingIds,
  Set<String> extraIds,
  int duplicateRecords,
  BigDecimal expectedQuantity,
  BigDecimal actualQuantity,
  BigDecimal expectedTotalPrice,
  BigDecimal actualTotalPrice,
  boolean overallPass) {

  
   Map<String, PositionReport> expectedMap =
           createReportMap(expectedReports);

   Map<String, PositionReport> actualMap =
           createReportMap(actualReports);

   Set<String> allIds = new HashSet<>();
   allIds.addAll(expectedMap.keySet());
   allIds.addAll(actualMap.keySet());

   List<String> sortedIds = new ArrayList<>(allIds);
   sortedIds.sort(String::compareTo);

   StringBuilder html = new StringBuilder();

   html.append("""
           <!DOCTYPE html>
           <html>
           <head>
               <meta charset="UTF-8">
               <title>Financial Data Reconciliation Summary</title>
               <style>
                   body {
                       font-family: Arial, sans-serif;
                       margin: 30px;
                       background: #f7f7f7;
                   }

                   h1, h2 {
                       color: #333;
                   }

                   .summary {
                       background: white;
                       padding: 20px;
                       border-radius: 8px;
                       margin-bottom: 25px;
                   }

                   table {
                       border-collapse: collapse;
                       width: 100%;
                       background: white;
                       margin-bottom: 25px;
                   }

                   th, td {
                       border: 1px solid #ddd;
                       padding: 10px;
                       text-align: left;
                   }

                   th {
                       background: #eeeeee;
                   }

                   .pass {
                       font-weight: bold;
                   }

                   .fail {
                       font-weight: bold;
                   }

                   .overall {
                       font-size: 22px;
                       padding: 15px;
                       margin-top: 20px;
                       background: white;
                       border-radius: 8px;
                   }
               </style>
           </head>
           <body>
           """);

   html.append("<h1>Financial Data Reconciliation Summary</h1>");

   html.append("<div class='summary'>");

   html.append("<h2>Reconciliation Summary</h2>");

   html.append("<table>");
   html.append("<tr><th>Metric</th><th>Value</th></tr>");

   appendRow(
           html,
           "Total Source Positions",
           String.valueOf(sourcePositionCount)
   );

   appendRow(
           html,
           "Total Expected Records",
           String.valueOf(expectedReports.size())
   );

   appendRow(
           html,
           "Total Actual Records",
           String.valueOf(actualReports.size())
   );

   appendRow(
           html,
           "Missing Records",
           String.valueOf(missingIds.size())
   );

   appendRow(
           html,
           "Extra Records",
           String.valueOf(extraIds.size())
   );

   appendRow(
           html,
           "Duplicate Records",
           String.valueOf(duplicateRecords)
   );

   appendRow(
           html,
           "Expected Total Quantity",
           expectedQuantity.toPlainString()
   );

   appendRow(
           html,
           "Actual Total Quantity",
           actualQuantity.toPlainString()
   );

   appendRow(
           html,
           "Expected Total Price",
           expectedTotalPrice.toPlainString()
   );

   appendRow(
           html,
           "Actual Total Price",
           actualTotalPrice.toPlainString()
   );

   html.append("</table>");

   String result = overallPass ? "PASS" : "FAIL";

   html.append("<div class='overall'>");
   html.append("Overall Result: <span class='")
           .append(overallPass ? "pass" : "fail")
           .append("'>")
           .append(result)
           .append("</span>");
   html.append("</div>");

   html.append("</div>");

   appendFailedRecords(
           html,
           expectedMap,
           actualMap,
           sortedIds
   );

   html.append("</body></html>");

   return html.toString();
  

  }

  private static Map<String, PositionReport> createReportMap(
  List<PositionReport> reports) {
   Map<String, PositionReport> map = new HashMap<>();
   for (PositionReport report : reports) {
       if (report.getPositionId() != null
               && !map.containsKey(report.getPositionId())) {

           map.put(report.getPositionId(), report);
       }
   }
   return map;
  }

  private static void appendFailedRecords(
  StringBuilder html,
  Map<String, PositionReport> expectedMap,
  Map<String, PositionReport> actualMap,
  List<String> allIds) {

  
   html.append("<h2>Record-Level Reconciliation</h2>");

   html.append("<table>");
   html.append("""
           <tr>
               <th>Position ID</th>
               <th>Expected ISIN</th>
               <th>Actual ISIN</th>
               <th>Expected Quantity</th>
               <th>Actual Quantity</th>
               <th>Expected Total Price</th>
               <th>Actual Total Price</th>
               <th>Result</th>
           </tr>
           """);

   for (String positionId : allIds) {

       PositionReport expected = expectedMap.get(positionId);
       PositionReport actual = actualMap.get(positionId);

       boolean matches = recordsMatch(expected, actual);

       html.append("<tr>");
       appendCell(
               html,
               positionId
       );

       appendCell(
               html,
               expected == null ? "-" : value(expected.getIsin())
       );

       appendCell(
               html,
               actual == null ? "-" : value(actual.getIsin())
       );

       appendCell(
               html,
               expected == null
                       ? "-"
                       : value(expected.getQuantity())
       );

       appendCell(
               html,
               actual == null
                       ? "-"
                       : value(actual.getQuantity())
       );

       appendCell(
               html,
               expected == null
                       ? "-"
                       : value(expected.getTotalPrice())
       );

       appendCell(
               html,
               actual == null
                       ? "-"
                       : value(actual.getTotalPrice())
       );

       appendCell(
               html,
               matches ? "PASS" : "FAIL"
       );

       html.append("</tr>");
   }

   html.append("</table>");
  

  }

  private static boolean recordsMatch(
  PositionReport expected,
  PositionReport actual) {

  
   if (expected == null || actual == null) {
       return false;
   }

   return equalsIgnoreNull(
           expected.getIsin(),
           actual.getIsin()
   )
           && equalsBigDecimal(
           expected.getQuantity(),
           actual.getQuantity()
   )
           && equalsBigDecimal(
           expected.getTotalPrice(),
           actual.getTotalPrice()
   );
  

  }

  private static boolean equalsIgnoreNull(
  String expected,
  String actual) {

  
   if (expected == null) {
       return actual == null;
   }

   return expected.equals(actual);
  

  }

  private static boolean equalsBigDecimal(
  BigDecimal expected,
  BigDecimal actual) {

  
   if (expected == null) {
       return actual == null;
   }

   return actual != null
           && expected.compareTo(actual) == 0;
  

  }

  private static String value(Object value) {
  return value == null ? "-" : value.toString();
  }

  private static void appendRow(
  StringBuilder html,
  String name,
  String value) {

  
   html.append("<tr>");
   html.append("<td>")
           .append(name)
           .append("</td>");
   html.append("<td>")
           .append(value)
           .append("</td>");
   html.append("</tr>");
  

  }

  private static void appendCell(
  StringBuilder html,
  String value) {

  
   html.append("<td>")
           .append(value)
           .append("</td>");
  

  }
  }
