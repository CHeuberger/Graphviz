/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import java.awt.Dimension;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.util.TableOrder;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.general.DefaultKeyedValues2DDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.BoxAndWhiskerXYDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerXYDataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.CategoryTableXYDataset;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.MatrixSeries;
import org.jfree.data.xy.MatrixSeriesCollection;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XIntervalSeries;
import org.jfree.data.xy.XIntervalSeriesCollection;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYZDataset;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;

/**
 * @author Carlos F. Heuberger, 2022-11-23
 *
 */
public class JFreeChartSample {

    public static void main(String[] args) {
        var clear = false;
        for (var i = 0; i < args.length; i++) {
            var arg = args[i];
            if (arg.equals("-clear")) {
                clear = true;
            } else {
                usage("unrecognized argument: " + arg);
                return;
            }
        }
        var test = new JFreeChartSample(clear);
        test.show();
    }
    
    private static void usage(String message) {
        if (message != null) {
            System.err.println(message);
        }
        System.out.printf("""
            Usage: java %s [-clear]
            Options:
                -clear    clear preferences
            """, 
            JFreeChartSample.class.getSimpleName());
    }
    
    private static final String PREF_TAB_PREFIX = "tab.";
    private static final String PREF_TAB_TABS = PREF_TAB_PREFIX + "tabs";
    private static final String PREF_TAB_AREA = PREF_TAB_PREFIX + "area";
    private static final String PREF_TAB_BAR = PREF_TAB_PREFIX + "bar";
    private static final String PREF_TAB_BOXWHISKERS = PREF_TAB_PREFIX + "boxAndWhiskers";
    private static final String PREF_TAB_BUBBLE = PREF_TAB_PREFIX + "bubble";
    private static final String PREF_TAB_CANDLE = PREF_TAB_PREFIX + "candle";
    private static final String PREF_TAB_GANTT = PREF_TAB_PREFIX + "gantt";
    private static final String PREF_TAB_HIGHLOW = PREF_TAB_PREFIX + "highLow";
    private static final String PREF_TAB_HISTOGRAM = PREF_TAB_PREFIX + "histogram";
    private static final String PREF_TAB_LINE = PREF_TAB_PREFIX + "line";
    private static final String PREF_TAB_MULTI_PIE = PREF_TAB_PREFIX + "multiPie";
    private static final String PREF_TAB_MULTI_PIE_3D = PREF_TAB_PREFIX + "multiPie3D";
    private static final String PREF_TAB_PIE = PREF_TAB_PREFIX + "pie";
    private static final String PREF_TAB_POLAR = PREF_TAB_PREFIX + "polar";
    private static final String PREF_TAB_RING = PREF_TAB_PREFIX + "ring";
    private static final String PREF_TAB_SCATTER = PREF_TAB_PREFIX + "scatter";
    private static final String PREF_TAB_STACKED_AREA = PREF_TAB_PREFIX + "stackedArea";
    
    private final Preferences preferences = Preferences.userNodeForPackage(getClass());
    
    private final JFrame frame;
    private final JTabbedPane tabs;
    private final JTabbedPane areas;
    private final JTabbedPane bars;
    private final JTabbedPane boxAndWhiskers;
    private final JTabbedPane bubbles;
    private final JTabbedPane candles;
    private final JTabbedPane gantts;
    private final JTabbedPane highlows;
    private final JTabbedPane histograms;
    private final JTabbedPane lines;
    private final JTabbedPane multiPies;
    private final JTabbedPane multiPies3D;
    private final JTabbedPane pies;
    private final JTabbedPane polars;
    private final JTabbedPane rings;
    private final JTabbedPane scatters;
    private final JTabbedPane stackedAreas;
    
    private JFreeChartSample(boolean clear) {
        if (clear) {
            try {
                preferences.clear();
            } catch (BackingStoreException ex) {
                ex.printStackTrace();
            }
        }
        
        areas = newJTabbedPane(PREF_TAB_AREA);
        categoryDS(this::area);
        
        bars = newJTabbedPane(PREF_TAB_BAR);
        categoryDS(this::bars);
        
        boxAndWhiskers = newJTabbedPane(PREF_TAB_BOXWHISKERS);
        boxAndWhiskers(boxAndWhiskerCategory());
        boxAndWhiskers(boxAndWhiskerXY());

        bubbles = newJTabbedPane(PREF_TAB_BUBBLE);
        bubbles(xyz());
        bubbles(matrix());
        
        candles = newJTabbedPane(PREF_TAB_CANDLE);
        ohlcDS(this::candles);
        
        gantts = newJTabbedPane(PREF_TAB_GANTT);
        gantts(intervalCategory());
        gantts(task());
        
        highlows = newJTabbedPane(PREF_TAB_HIGHLOW);
        ohlcDS(this::highlows);
        
        histograms = newJTabbedPane(PREF_TAB_HISTOGRAM);
        intervalXYDS(this::histograms);
        
        lines = newJTabbedPane(PREF_TAB_LINE);
        categoryDS(this::lines);
        
        multiPies = newJTabbedPane(PREF_TAB_MULTI_PIE);
        categoryDS(this::multiPies);
        
        multiPies3D = newJTabbedPane(PREF_TAB_MULTI_PIE_3D);
        categoryDS(this::multiPies3D);
        
        pies = newJTabbedPane(PREF_TAB_PIE);
        piesDS(this::pies);
        
        polars = newJTabbedPane(PREF_TAB_POLAR);
        xyDS(this::polars);
        
        rings = newJTabbedPane(PREF_TAB_RING);
        piesDS(this::rings);
        
        scatters = newJTabbedPane(PREF_TAB_SCATTER);
        xyDS(this::scatters);
        
        stackedAreas = newJTabbedPane(PREF_TAB_STACKED_AREA);
        categoryDS(this::stackedAreas);
        
        tabs = newJTabbedPane(PREF_TAB_TABS);
        tabs.add("AREA", areas);
        tabs.add("BAR", bars);
        tabs.add("BOX WHISKERS", boxAndWhiskers);
        tabs.add("BUBBLES", bubbles);
        tabs.add("CANDLES", candles);
        tabs.add("GANTTS", gantts);
        tabs.add("HIGH LOW", highlows);
        tabs.add("HISTOGRAM", histograms);
        tabs.add("LINE", lines);
        tabs.add("MULTI PIE", multiPies);
        tabs.add("MULTI PIE 3D", multiPies3D);
        tabs.add("PIE", pies);
        tabs.add("POLAR", polars);
        tabs.add("RING", rings);
        tabs.add("SCATTER", scatters);
        tabs.add("ST AREA", stackedAreas);
        
        frame = new JFrame();
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.add(tabs);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
    
    private void show() {
        frame.setVisible(true);
    }
    
    private ChartPanel panel(JFreeChart chart) {
        var panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(800, 500));
        return panel;
    }

    private JTabbedPane newJTabbedPane(String key) {
        @SuppressWarnings("serial")
        var pane = new JTabbedPane() {
            @Override
            public void addNotify() {
                super.addNotify();
                var index = preferences.getInt(getName(), getTabCount()-1);
                setSelectedIndex(index);
                addChangeListener(JFreeChartSample.this::tabChanged);
            }
        };
        pane.setName(key);
        return pane;
    }
    
    private void tabChanged(ChangeEvent ev) {
        if (ev.getSource() instanceof JTabbedPane tab) {
            var index = tab.getSelectedIndex();
            preferences.putInt(tab.getName(), index);
        } else {
            System.err.println(ev);
        }
    }
    
    //----------------------------------------------------------------------------------------------
    
    private void categoryDS(Consumer<CategoryDataset> factory) {
        factory.accept(boxAndWhiskerCategory());
        factory.accept(task());
        factory.accept(category());
        factory.accept(intervalCategory());
        factory.accept(keyed2D());
        factory.accept(multiValue());
        factory.accept(statistical());
    }
    
    private void ohlcDS(Consumer<OHLCDataset> factory) {
        factory.accept(highLow());
        factory.accept(ohlc());
        factory.accept(ohlcSeries());
    }
    
    private void intervalXYDS(Consumer<IntervalXYDataset> factory) {
        factory.accept(categorytableXY());
        factory.accept(intervalXY());
        factory.accept(tableXY());
        factory.accept(histogram());
        factory.accept(simpleHist());
        factory.accept(timePeriod());
        factory.accept(timeSeries());
        factory.accept(timeTableXY());
        factory.accept(xInterval());
        factory.accept(xyBar());
        factory.accept(xyInterval());
        factory.accept(xySeries());
        factory.accept(xyTask());
        factory.accept(yInterval());
    }
    
    private void piesDS(BiConsumer<PieDataset<?>, PieDataset<?>> factory) {
        factory.accept(catPie(category()), null);
        factory.accept(pie(), null);
        factory.accept(pie(), pie2());
    }
    
    private void xyDS(Consumer<XYDataset> factory) {
        factory.accept(categorytableXY());
        factory.accept(intervalXY());
        factory.accept(tableXY());
        factory.accept(histogram());
        factory.accept(simpleHist());
        factory.accept(timePeriod());
        factory.accept(timeSeries());
        factory.accept(timeTableXY());
        factory.accept(xInterval());
        factory.accept(xyBar());
        factory.accept(xyInterval());
        factory.accept(xySeries());
        factory.accept(xyTask());
        factory.accept(yInterval());
    }
    
    //----------------------------------------------------------------------------------------------
    
    private void area(CategoryDataset dataset) {
        String title = "%d-%s".formatted(areas.getTabCount()+1, dataset.getClass().getSimpleName());
        var chart = ChartFactory.createAreaChart(title, "category", "value", dataset);
        if (dataset instanceof TaskSeriesCollection) {
            chart.getCategoryPlot().setRangeAxis(new DateAxis());
        }

        areas.addTab(chart.getTitle().getText(), panel(chart));
    }
    
    private void bars(CategoryDataset dataset) {
        String title = "%d-%s".formatted(bars.getTabCount()+1, dataset.getClass().getSimpleName());
        var chart = ChartFactory.createBarChart(title, "category", "value", dataset);
        if (dataset instanceof TaskSeriesCollection) {
            chart.getCategoryPlot().setRangeAxis(new DateAxis());
        }
        
        bars.addTab(chart.getTitle().getText(), panel(chart));
    }

    private void boxAndWhiskers(BoxAndWhiskerCategoryDataset dataset) {
        String title = "%d-%s".formatted(boxAndWhiskers.getTabCount()+1, dataset.getClass().getSimpleName());
        var chart = ChartFactory.createBoxAndWhiskerChart(title, "category", "value", dataset, true);

        boxAndWhiskers.addTab(chart.getTitle().getText(), panel(chart));
    }
    
    private void boxAndWhiskers(BoxAndWhiskerXYDataset dataset) {
        String title = "%d-%s".formatted(boxAndWhiskers.getTabCount()+1, dataset.getClass().getSimpleName());
        var chart = ChartFactory.createBoxAndWhiskerChart(title, "time", "value", dataset, true);
        
        boxAndWhiskers.addTab(chart.getTitle().getText(), panel(chart));
    }
    
    private void bubbles(XYZDataset dataset) {
        String title = "%d-%s".formatted(bubbles.getTabCount()+1, dataset.getClass().getSimpleName());
        var chart = ChartFactory.createBubbleChart(title, "X", "Y", dataset);
        
        bubbles.addTab(chart.getTitle().getText(), panel(chart));
    }
    
    private void candles(OHLCDataset dataset) {
        String title = "%d-%s".formatted(candles.getTabCount()+1, dataset.getClass().getSimpleName());
        var chart = ChartFactory.createCandlestickChart(title, "time", "value", dataset, true);
        
        candles.addTab(chart.getTitle().getText(), panel(chart));
    }
    
    private void gantts(IntervalCategoryDataset dataset) {
        String title = "%d-%s".formatted(gantts.getTabCount()+1, dataset.getClass().getSimpleName());
        var chart = ChartFactory.createGanttChart(title, "category", "date", dataset);
        
        gantts.addTab(chart.getTitle().getText(), panel(chart));
    }
    
    private void highlows(OHLCDataset dataset) {
        String title = "%d-%s".formatted(highlows.getTabCount()+1, dataset.getClass().getSimpleName());
        var chart = ChartFactory.createHighLowChart(title, "x", "y", dataset, true);
        
        highlows.addTab(chart.getTitle().getText(), panel(chart));
    }
    
    private void histograms(IntervalXYDataset dataset) {
        String title = "%d-%s".formatted(histograms.getTabCount()+1, dataset.getClass().getSimpleName());
        var chart = ChartFactory.createHistogram(title, "x", "y", dataset);
        if (dataset.getClass().getSimpleName().startsWith("Time")) {
            chart.getXYPlot().setDomainAxis(new DateAxis("test"));
        }
        if (dataset instanceof XYTaskDataset) {
            chart.getXYPlot().setRangeAxis(new DateAxis("test"));
        }
        
        histograms.addTab(chart.getTitle().getText(), panel(chart));
    }
    
    private void lines(CategoryDataset dataset) {
        String title = "%d-%s".formatted(lines.getTabCount()+1, dataset.getClass().getSimpleName());
        var chart = ChartFactory.createLineChart(title, "category", "value", dataset);
        if (dataset instanceof TaskSeriesCollection) {
            chart.getCategoryPlot().setRangeAxis(new DateAxis());
        }
        
        lines.addTab(chart.getTitle().getText(), panel(chart));
    }
    
    private void multiPies(CategoryDataset dataset) {
        if (dataset instanceof TaskSeriesCollection)
            return;
        String title = "%d-%s".formatted(multiPies.getTabCount()+1, dataset.getClass().getSimpleName());
        var chart = ChartFactory.createMultiplePieChart(title, dataset, TableOrder.BY_COLUMN, false, false, false);

        multiPies.addTab(chart.getTitle().getText(), panel(chart));
    }
    
    private void multiPies3D(CategoryDataset dataset) {
        if (dataset instanceof TaskSeriesCollection)
            return;
        String title = "%d-%s".formatted(multiPies3D.getTabCount()+1, dataset.getClass().getSimpleName());
        var chart = ChartFactory.createMultiplePieChart3D(title, dataset, TableOrder.BY_COLUMN, false, false, false);
        
        multiPies3D.addTab(chart.getTitle().getText(), panel(chart));
    }

    private void pies(PieDataset<?> dataset1, PieDataset<?> dataset2) {
        String title = "%d-%s".formatted(pies.getTabCount()+1, dataset1.getClass().getSimpleName());
        JFreeChart chart;
        if (dataset2 == null) {
            chart = ChartFactory.createPieChart(title, dataset1);
        } else {
            chart = ChartFactory.createPieChart(title, dataset1, dataset2, 50, true, true, true, false, false, true);
        }
        pies.addTab(chart.getTitle().getText(), panel(chart));
    }
    
    private void polars(XYDataset dataset) {
        String title = "%d-%s".formatted(polars.getTabCount()+1, dataset.getClass().getSimpleName());
        var chart = ChartFactory.createPolarChart(title, dataset, true, false, false);
        
        polars.addTab(chart.getTitle().getText(), panel(chart));
    }
    
    private void rings(PieDataset<?> dataset1, PieDataset<?> dataset2) {
        if (dataset2 == null) {
            String title = "%d-%s".formatted(rings.getTabCount()+1, dataset1.getClass().getSimpleName());
            var chart = ChartFactory.createRingChart(title, dataset1, true, false, false);

            rings.addTab(chart.getTitle().getText(), panel(chart));
        }
    }
    
    private void scatters(XYDataset dataset) {
        String title = "%d-%s".formatted(scatters.getTabCount()+1, dataset.getClass().getSimpleName());
        var chart = ChartFactory.createScatterPlot(title, "X", "Y", dataset);
        
        scatters.addTab(chart.getTitle().getText(), panel(chart));
    }
    
    private void stackedAreas(CategoryDataset dataset) {
        if (dataset instanceof TaskSeriesCollection)
            return;
        String title = "%d-%s".formatted(stackedAreas.getTabCount()+1, dataset.getClass().getSimpleName());
        var chart = ChartFactory.createStackedAreaChart(title, "X", "Y", dataset);
        
        stackedAreas.addTab(chart.getTitle().getText(), panel(chart));
    }
 
    //----------------------------------------------------------------------------------------------
    
    private DefaultBoxAndWhiskerCategoryDataset boxAndWhiskerCategory() {
        var dataset = new DefaultBoxAndWhiskerCategoryDataset();
        dataset.add(List.of(21, 22, 24), "R1", "C1");
        dataset.add(List.of(26, 27, 29), "R1", "C2");
        dataset.add(List.of(10, 12, 14), "R2", "C1");
        dataset.add(List.of(15, 17, 14), "R2", "C2");
        dataset.add(List.of(10, 11, 12, 18), "R1", "C1");
        return dataset;
    }
    
    @SuppressWarnings("deprecation")
    private DefaultBoxAndWhiskerXYDataset boxAndWhiskerXY() {
        var dataset = new DefaultBoxAndWhiskerXYDataset("BoxWhiskers");
        dataset.add(new Date("2022/11/23 10:20:00"), new BoxAndWhiskerItem(10, 9, 8, 12, 7, 14, 6, 16, List.of(6.5, 15, 15.5)));
        dataset.add(new Date("2022/11/23 11:20:00"), new BoxAndWhiskerItem(10, 9, 8, 12, 6, 16, 7, 14, null));
        dataset.add(new Date("2022/11/24 11:20:00"), new BoxAndWhiskerItem(10, 9, 8, 12, 6, 16, 7, 14, null));
        return dataset;
    }
    
    private DefaultCategoryDataset category() {
        var dataset = new DefaultCategoryDataset();
        dataset.addValue(21, "R1", "C1");
        dataset.addValue(22, "R1", "C2");
        dataset.addValue(24, "R1", "C3");
        dataset.addValue(10, "R2", "C1");
        dataset.addValue(12, "R2", "C2");
        return dataset;
    }
    
    private DefaultIntervalCategoryDataset intervalCategory() {
        var starts = new double[][] { 
            { 10, 21, 23.5 },
            { 15, 17, 25 },
        };
        var ends = new double[][] { 
            { 20, 23, 24 },
            { 18, 22, 17 },
        };
        var dataset = new DefaultIntervalCategoryDataset(starts, ends);
        return dataset;
    }
    
    private DefaultKeyedValues2DDataset keyed2D() {
        var dataset = new DefaultKeyedValues2DDataset();
        dataset.addValue(21, "R1", "C1");
        dataset.addValue(22, "R1", "C2");
        dataset.addValue(24, "R1", "C3");
        dataset.addValue(10, "R2", "C1");
        dataset.addValue(12, "R2", "C2");
        return dataset;
    }
    
    private DefaultMultiValueCategoryDataset multiValue() {
        var dataset = new DefaultMultiValueCategoryDataset();
        dataset.add(List.of(20, 23, 26), "R1", "C1");
        dataset.add(List.of(16, 17), "R2", "C1");
        dataset.add(List.of(10, 14), "R2", "C2");
        return dataset;
    }
    
    private DefaultStatisticalCategoryDataset statistical() {
        var dataset = new DefaultStatisticalCategoryDataset();
        dataset.add(21, 1.2, "R1", "C1");
        dataset.add(22, 1.1, "R1", "C2");
        dataset.add(24, 1.3, "R1", "C3");
        dataset.add(10, 1.0, "R2", "C1");
        dataset.add(12, 0.9, "R2", "C2");
        return dataset;
    }
    
    @SuppressWarnings("deprecation")
    private TaskSeriesCollection task() {
        var dataset = new TaskSeriesCollection();
        var series1 = new TaskSeries("Series 1");
        series1.add(new Task("Task 11", new Date("2022/11/23 10:00:00"), new Date("2022/11/24 12:00:00")));
        series1.add(new Task("Task 12", new Date("2022/11/23 16:00:00"), new Date("2022/11/25 12:00:00")));
        series1.add(new Task("Task 13", new Date("2022/11/24 10:00:00"), new Date("2022/11/25 12:00:00")));
        dataset.add(series1);
        var series2 = new TaskSeries("Series 2");
        series2.add(new Task("Task 21", new Date("2022/11/24 14:00:00"), new Date("2022/11/24 18:00:00")));
        series2.add(new Task("Task 22", new Date("2022/11/25 10:00:00"), new Date("2022/11/25 16:00:00")));
        dataset.add(series2);
        return dataset;
    }
    
    private DefaultXYZDataset xyz() {
        var dataset = new DefaultXYZDataset();
        dataset.addSeries("Series 1", new double[][] {
            {10, 12, 14},
            {15, 16, 18},
            {0.2, 0.2, 0.5}
        });
        dataset.addSeries("Series 2", new double[][] {
                {11, 11, 13, 14},
                {15.5, 16.5, 17, 17.7},
                {0.5, 0.2, 0.8, 0.3}
        });
        return dataset;
    }
    
    private MatrixSeriesCollection matrix() {
        var dataset = new MatrixSeriesCollection();
        var series1 = new MatrixSeries("Series 1", 2, 3);
        series1.update(1, 0, 0.1);
        series1.update(0, 1, 0.2);
        series1.update(1, 1, 0.1);
        series1.update(0, 2, 0.1);
        series1.update(1, 2, 0.3);
        dataset.addSeries(series1);
        var series2 = new MatrixSeries("Series 2", 1, 2);
        series2.update(0, 0, 0.2);
        series2.update(0, 1, 0.4);
        dataset.addSeries(series2);
        return dataset;
    }
    
    @SuppressWarnings("deprecation")
    private DefaultHighLowDataset highLow() {
        var date = new Date[] { new Date("2022/11/23 10:00:00"), new Date("2022/11/23 11:00:00") };
        var high = new double[] { 20, 22 };
        var low = new double[] { 10, 13 };
        var open = new double[] {12, 14 };
        var close = new double[] { 19, 19 };
        var volume = new double[] { 1, 3 };
        var dataset = new DefaultHighLowDataset("Series 1", date, high, low, open, close, volume);
        return dataset;
    }
    
    @SuppressWarnings("deprecation")
    private DefaultOHLCDataset ohlc() {
        var data = new OHLCDataItem[] {
                new OHLCDataItem(new Date("2022/11/23 10:00:00"), 12, 20, 10, 19, 1),
                new OHLCDataItem(new Date("2022/11/23 11:00:00"), 14, 22, 13, 19, 3),
        };
        var dataset = new DefaultOHLCDataset("Series 1", data);
        return dataset;
    }
    
    private OHLCSeriesCollection ohlcSeries() {
        var dataset = new OHLCSeriesCollection();
        var series1 = new OHLCSeries("Series 1");
        series1.add(new Hour(10, 23, 10, 2022), 12, 20, 10, 19);
        series1.add(new Hour(11, 23, 10, 2022), 14, 22, 13, 19);
        dataset.addSeries(series1);
        var series2 = new OHLCSeries("Series 2");
        series2.add(new Hour(11, 23, 10, 2022), 7, 13, 5, 10);
        series2.add(new Hour(12, 23, 10, 2022), 14, 22, 13, 19);
        dataset.addSeries(series2);
        return dataset;
    }
    
    private CategoryTableXYDataset categorytableXY() {
        var dataset = new CategoryTableXYDataset();
        dataset.add(20, 2, "series 1");
        dataset.add(30, 2, "series 1");
        dataset.add(40, 3, "series 1");
        dataset.add(50, 3.5, "series 1");
        dataset.add(22, 1, "series 2");
        dataset.add(30, 2.5, "series 2");
        dataset.add(41, 2.5, "series 2");
        return dataset;
    }
    
    private DefaultIntervalXYDataset intervalXY() {
        var dataset = new DefaultIntervalXYDataset();
        dataset.addSeries("Series 1", new double[][] {
            {15, 20},  // x
            {14, 18},   // start x
            {16, 23},  // end x
            {15, 17},  // y
            {14, 16},  // start y
            {16, 19}  // end y
        });
        dataset.addSeries("Series 2", new double[][] {
            {16, 26},  // x
            {15, 24},   // start x
            {17, 27},  // end x
            {16, 15},  // y
            {14, 14},  // start y
            {17, 17}  // end y
        });
        return dataset;
    }
    
    private DefaultTableXYDataset tableXY() {
        var dataset = new DefaultTableXYDataset();
        var series1 = new XYSeries("Series 1", false, false);
        series1.add(10, 10);
        series1.add(12, 10);
        series1.add(14, 12);
        dataset.addSeries(series1);
        var series2 = new XYSeries("Series 2", false, false);
        series2.add(10, 12);
        series2.add(13, 9);
        series2.add(15, 15);
        dataset.addSeries(series2);
        return dataset;
    }
    
    private HistogramDataset histogram() {
        var dataset = new HistogramDataset();
        dataset.addSeries("series 1", new double[] { 10, 12, 13, 14, 14.5, 15 }, 3);
        dataset.addSeries("series 2", new double[] { 20, 21, 22, 22.5, 23, 24, 24.5, 25, 25.5 }, 4);
        return dataset;
    }
    
    private SimpleHistogramDataset simpleHist() {
        var dataset = new SimpleHistogramDataset("series 1");
        dataset.addBin(new SimpleHistogramBin(10, 19.9));
        dataset.addObservation(12);
        dataset.addObservation(13);
        dataset.addBin(new SimpleHistogramBin(20, 29.9));
        dataset.addObservation(21);
        return dataset;
    }
    
    private TimePeriodValuesCollection timePeriod() {
        var dataset = new TimePeriodValuesCollection();
        var series1 = new TimePeriodValues("Series 1");
        series1.add(new Hour(10, 23, 10, 2022), 10);
        series1.add(new Hour(12, 23, 10, 2022), 12);
        dataset.addSeries(series1);
        return dataset;
    }
    
    private TimeSeriesCollection timeSeries() {
        var dataset = new TimeSeriesCollection();
        var series1 = new TimeSeries("Series 1");
        series1.add(new Hour(10, 23, 10, 2022), 10);
        series1.add(new Hour(12, 23, 10, 2022), 12);
        dataset.addSeries(series1);
        var series2 = new TimeSeries("Series 2");
        series2.add(new Hour(11, 23, 10, 2022), 14);
        series2.add(new Hour(14, 23, 10, 2022), 11);
        dataset.addSeries(series2);
        return dataset;
    }
    
    private TimeTableXYDataset timeTableXY() {
        var dataset = new TimeTableXYDataset();
        dataset.add(new Hour(10, 23, 10, 2022), 10, "series 1");
        dataset.add(new Hour(12, 23, 10, 2022), 12, "series 1");
        dataset.add(new Hour(11, 23, 10, 2022), 10, "series 2");
        dataset.add(new Hour(14, 23, 10, 2022), 9, "series 2");
        return dataset;
    }
    
    private XIntervalSeriesCollection xInterval() {
        var dataset = new XIntervalSeriesCollection();
        var series1 = new XIntervalSeries("Series 1");
        series1.add(15, 12, 16, 10);
        series1.add(18, 17, 20, 12);
        dataset.addSeries(series1);
        var series2 = new XIntervalSeries("Series 2");
        series2.add(19, 18, 21, 20);
        series2.add(23, 22, 24, 15);
        dataset.addSeries(series2);
        return dataset;
    }
    
    private XYBarDataset xyBar() {
        var dataset = new XYBarDataset(xy(), 1);
        return dataset;
    }
    
    private DefaultXYDataset xy() {
        var dataset = new DefaultXYDataset();
        dataset.addSeries("series 1", new double[][] {
            {10, 12, 15},
            {11, 11, 13}
        });
        dataset.addSeries("series 2", new double[][] {
            {11, 13, 15.5},
            {12, 10, 15}
        });
        return dataset;
    }
    
    private XYIntervalSeriesCollection xyInterval() {
        var dataset = new XYIntervalSeriesCollection();
        var series1 = new XYIntervalSeries("Series 1");
        series1.add(15, 14.5, 15.5, 15, 14, 16);
        series1.add(17, 16, 18, 13, 12.5, 13);
        dataset.addSeries(series1);
        var series2 = new XYIntervalSeries("Series 2");
        series2.add(18, 17.7, 18.4, 14, 13, 15);
        series2.add(20, 19.8, 20.2, 12, 11, 13);
        dataset.addSeries(series2);
        return dataset;
    }
    
    private XYSeriesCollection xySeries() {
        var dataset = new XYSeriesCollection();
        var series1 = new XYSeries("Series 1", false, false);
        series1.add(10, 10);
        series1.add(12, 10);
        series1.add(14, 12);
        dataset.addSeries(series1);
        var series2 = new XYSeries("Series 2", false, false);
        series2.add(10, 12);
        series2.add(13, 9);
        series2.add(15, 15);
        dataset.addSeries(series2);
        return dataset;
    }
    
    private XYTaskDataset xyTask() {
        var dataset = new XYTaskDataset(task());
        return dataset;
    }
    
    private YIntervalSeriesCollection yInterval() {
        var dataset = new YIntervalSeriesCollection();
        var series1 = new YIntervalSeries("Series 1");
        series1.add(10, 15, 14, 17);
        series1.add(11, 17, 15, 17.5);
        dataset.addSeries(series1);
        var series2 = new YIntervalSeries("Series 2");
        series2.add(11.5, 12, 11, 13);
        series2.add(12, 16, 14, 18);
        dataset.addSeries(series2);
        return dataset;
    }
    
    private CategoryToPieDataset catPie(CategoryDataset source) {
        var dataset = new CategoryToPieDataset(source, TableOrder.BY_COLUMN, 0);
        return dataset;
    }
    
    private DefaultPieDataset pie() {
        var dataset = new DefaultPieDataset<String>();
        dataset.insertValue(0, "key 1", 0.8);
        dataset.insertValue(0, "key 2", 2.4);
        return dataset;
    }
    
    private DefaultPieDataset pie2() {
        var dataset = new DefaultPieDataset<String>();
        dataset.insertValue(0, "key 1", 1.0);
        dataset.insertValue(0, "key 2", 2.67);
        return dataset;
    }
}
