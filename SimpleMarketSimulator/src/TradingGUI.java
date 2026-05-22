/*  TradingGUI.java
    Deskripsi : MT5-style GUI for JRC Broker Trading Platform
    How to run: Place this file alongside your backend .java files and compile all together:
                javac *.java
                java TradingGUI

    ============================================================
    HOW TO CONNECT TO YOUR BACKEND — READ THIS CAREFULLY
    ============================================================
    Every section that needs wiring to your backend is marked with:
        // [CONNECT] — what to do and which class/method to call
    ============================================================
*/

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.security.PrivateKey;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class TradingGUI extends JFrame {

    // ============================================================
    // [CONNECT] — Replace these dummy fields with your real objects
    // ============================================================
    // Example:
    //   private Investor currentInvestor;
    //   private Saham bbca;
    //   private Crypto btc;
    //   private Portofolio porto;
        private Saham bbca;
        private Saham tlkm;
        private Crypto btc;
        private Crypto eth;
        private Forex eurousd;
        private Forex usdjpy;
        private Index spx;
        private Index ndq;
        private Komoditas emas;
        private Komoditas minyak;
        private Investor trump;
        private Investor johan;
        private Investor netanyahu;
    // Then in the constructor below, initialize them like Main.java does:
    //   currentInvestor = new Investor("INV001", "trump", 500000);
    //   bbca = new Saham("BBCA","Bank BCA",9500,1200,1500,24655010000L);
    // ============================================================

    // ── Color Palette (MT5 dark theme) ──────────────────────────
    static final Color BG_DARK      = new Color(0x12, 0x14, 0x18);
    static final Color BG_PANEL     = new Color(0x1A, 0x1D, 0x23);
    static final Color BG_WIDGET    = new Color(0x22, 0x26, 0x2E);
    static final Color BG_ROW_ALT   = new Color(0x1E, 0x21, 0x28);
    static final Color ACCENT_BLUE  = new Color(0x23, 0x8B, 0xFA);
    static final Color ACCENT_GREEN = new Color(0x00, 0xD4, 0x7E);
    static final Color ACCENT_RED   = new Color(0xFF, 0x45, 0x5A);
    static final Color TEXT_PRIMARY = new Color(0xE8, 0xEC, 0xF0);
    static final Color TEXT_MUTED   = new Color(0x6B, 0x72, 0x80);
    static final Color BORDER_COLOR = new Color(0x2D, 0x33, 0x3D);
    static final Color GOLD         = new Color(0xF5, 0xAA, 0x1C);

    static final Font FONT_MONO    = new Font("Courier New",  Font.PLAIN,  12);
    static final Font FONT_LABEL   = new Font("SansSerif",    Font.PLAIN,  11);
    static final Font FONT_BOLD    = new Font("SansSerif",    Font.BOLD,   12);
    static final Font FONT_TITLE   = new Font("SansSerif",    Font.BOLD,   13);
    static final Font FONT_TICKER  = new Font("Courier New",  Font.BOLD,   14);
    static final Font FONT_PRICE   = new Font("Courier New",  Font.BOLD,   18);
    static final Font FONT_SMALL   = new Font("SansSerif",    Font.PLAIN,  10);

    DecimalFormat df2 = new DecimalFormat("#,##0.00");
    DecimalFormat df5 = new DecimalFormat("#,##0.00000");

    // ── State ────────────────────────────────────────────────────
    // [CONNECT] Replace with your real Instrumen array
    String[] instruments   = {"BBCA","TLKM","BTCUSD","ETHUSD","EURUSD","USDJPY","SPX","NDQ","Gold","BZ"};
    String[] instTypes     = {"Saham","Saham","Crypto","Crypto","Forex","Forex","Index","Index","Komoditas","Komoditas"};
    double[] prices        = {9500, 3800, 67000, 2076, 1.147, 155.906, 6340, 22950, 4500, 112};
    double[] prevPrices    = prices.clone();
    int selectedInstrument = 0;

    // [CONNECT] Replace with data from your Investor and Portofolio
    double balance    = 500000;
    double equity     = 500000;
    double freeMargin = 500000;
    double usedMargin = 0;

    // Chart data
    ArrayList<Double> chartData = new ArrayList<>();
    Random rng = new Random();

    // Swing components we update dynamically
    JLabel lblBalance, lblEquity, lblFreeMargin, lblUsedMargin;
    JLabel lblCurrentPrice, lblCurrentTicker, lblCurrentType;
    JTable portfolioTable, marketTable;
    DefaultTableModel portfolioModel, marketModel;
    ChartPanel chartPanel;
    JLabel lblStatus;
    JComboBox<String> cmbInstrument;
    JTextField tfVolume, tfLeverage, tfSL, tfTP;

    // ── Entry Point ──────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new TradingGUI().setVisible(true);
        });
    }

    // ── Constructor ──────────────────────────────────────────────
    public TradingGUI() {
        setTitle("JRC Broker — Trading Terminal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1400, 860);
        setMinimumSize(new Dimension(1100, 700));
        setBackground(BG_DARK);

        // [CONNECT] Initialize your backend objects here, e.g.:
        //   currentInvestor = new Investor("INV001", "trump", 500000);
        //   bbca = new Saham(...);
        //   trump.beli("TRX101", bbca, 10, 1.0, "LONG");
        //   balance    = currentInvestor's balance (you'd need a getter)
        //   equity     = same
        //   freeMargin = same
        //Declaring ticker
        bbca = new Saham("BBCA", "Bank BCA", 9500, 1200, 1500,24655010000L);
        tlkm = new Saham("TLKM", "Telkom Indonesia", 3800, 200, 400,99062216600L);
        btc = new Crypto("BTCUSD", "Bitcoin / US Dolar", 67000, 20000000L);
        eth = new Crypto("ETHUSD", "Ethereum / US Dollar", 2076, 20000000L); 
        eurousd = new Forex("EURUSD", "Euro / US Dollar", 1.147);
        usdjpy = new Forex("USDJPY", "US Dollar / Japanese Yen ", 155.906);
        spx = new Index("SPX", "S&P500", 6340);
        ndq = new Index("NDQ", "Nasdaq 100", 22950);
        emas = new Komoditas("Gold", "Gold", 4500);
        minyak = new Komoditas("BZ", "Brent Crude Oil", 112);
        //Declaring investor
        trump  = new Investor("INV001", "trump",  500000);
        netanyahu  = new Investor("INV002", "netanyahu",  500000);
        johan = new Investor("INV003", "Johan", 500000);

        //Transaksi
        trump.beli("TRX101", bbca, 10, 1.0, "LONG");
        trump.beli("TRX102", emas, 0.5, 5.0, "SHORT");
        trump.beli("TRX103", spx, 0.5, 5.0, "LONG");

        netanyahu.beli("TRX201", tlkm, 20, 2.0, "LONG");
        netanyahu.beli("TRX202", btc, 2, 3.0, "LONG");
        netanyahu.beli("TRX203",ndq,2,6.0,"SHORT");

        johan.beli("TRX301", bbca, 5, 1.0, "SHORT");
        johan.beli("TRX302",usdjpy,0.1,5.0,"LONG");
        johan.beli("TRX303", minyak, 5, 5.0, "LONG");


        // seed chart
        double seed = prices[selectedInstrument];
        for (int i = 0; i < 80; i++) {
            seed *= 1 + (rng.nextDouble() * 0.02 - 0.01);
            chartData.add(seed);
        }

        buildUI();
        Instrumen[] instrumen = {bbca,tlkm,btc,usdjpy,spx,ndq,emas,minyak};
        double[][] hargaSelanjutnya = {
            {9700, 9900, 10100, 10300, 10600}, //bbca
            {3900, 3850, 3950, 4000, 4100}, //tlkm
            {65170,63280,74148,68000,65940}, //btc
            {157.885,156.960,158.090,157.380,159.030}, //usdjpy
            {}, //spx
            {23363,24380,24811}, //ndq
            {4561,4731,4291,4185,4651}, //emas
            {105,102,99.5,110,120}, //minyak
        };
        ArrayList<Investor> semuaInvestor = new ArrayList<>();
        semuaInvestor.add(trump);
        semuaInvestor.add(netanyahu);
        semuaInvestor.add(johan);
        netanyahu.getPorto().simulasiBeberapa(instrumen,hargaSelanjutnya, 5, 0.07, semuaInvestor);
        startMarketSimulation();   // remove  / replace with your simulasiBeberapa()
        setLocationRelativeTo(null);
    }

    // ════════════════════════════════════════════════════════════
    //  UI BUILD
    // ════════════════════════════════════════════════════════════
    void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG_DARK);
        setContentPane(root);

        root.add(buildTopBar(),    BorderLayout.NORTH);
        root.add(buildCenterArea(),BorderLayout.CENTER);
        root.add(buildStatusBar(), BorderLayout.SOUTH);
    }

    // ── Top Bar ──────────────────────────────────────────────────
    JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG_PANEL);
        bar.setBorder(BorderFactory.createMatteBorder(0,0,1,0,BORDER_COLOR));
        bar.setPreferredSize(new Dimension(0, 52));

        // Logo
        JLabel logo = new JLabel("  ◈ JRC BROKER");
        logo.setFont(new Font("SansSerif", Font.BOLD, 16));
        logo.setForeground(ACCENT_BLUE);
        bar.add(logo, BorderLayout.WEST);

        // Account strip
        JPanel strip = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        strip.setOpaque(false);

        lblBalance    = makeLabelPair(strip, "Balance",    df2.format(balance));
        lblEquity     = makeLabelPair(strip, "Equity",     df2.format(equity));
        lblFreeMargin = makeLabelPair(strip, "Free Margin",df2.format(freeMargin));
        lblUsedMargin = makeLabelPair(strip, "Used Margin",df2.format(usedMargin));

        // [CONNECT] Call updateAccountStrip() whenever Investor state changes
        bar.add(strip, BorderLayout.EAST);
        return bar;
    }

    JLabel makeLabelPair(JPanel parent, String title, String value) {
        JPanel cell = new JPanel(new BorderLayout(0,1));
        cell.setOpaque(false);
        JLabel lbl1 = new JLabel(title, SwingConstants.CENTER);
        lbl1.setFont(FONT_SMALL);
        lbl1.setForeground(TEXT_MUTED);
        JLabel lbl2 = new JLabel(value, SwingConstants.CENTER);
        lbl2.setFont(FONT_MONO);
        lbl2.setForeground(TEXT_PRIMARY);
        cell.add(lbl1, BorderLayout.NORTH);
        cell.add(lbl2, BorderLayout.SOUTH);
        parent.add(cell);
        return lbl2;  // return so we can update it
    }

    // ── Center ───────────────────────────────────────────────────
    JPanel buildCenterArea() {
        JPanel center = new JPanel(new BorderLayout(4, 0));
        center.setBackground(BG_DARK);

        // Left: market watch
        center.add(buildMarketWatch(), BorderLayout.WEST);

        // Middle: chart + order
        JPanel mid = new JPanel(new BorderLayout(0, 4));
        mid.setBackground(BG_DARK);
        mid.add(buildChartArea(),  BorderLayout.CENTER);
        mid.add(buildOrderPanel(), BorderLayout.SOUTH);
        center.add(mid, BorderLayout.CENTER);

        // Right: portfolio
        center.add(buildPortfolioPanel(), BorderLayout.EAST);
        return center;
    }

    // ── Market Watch (Left) ──────────────────────────────────────
    JPanel buildMarketWatch() {
        JPanel panel = darkPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(230, 0));
        panel.setBorder(compoundBorder("MARKET WATCH"));

        String[] cols = {"Symbol","Type","Price","Chg%"};
        marketModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c){ return false; }
        };
        JTable tbl = new JTable(marketModel);
        styleTable(tbl);
        tbl.setDefaultRenderer(Object.class, new MarketCellRenderer());
        tbl.setRowHeight(26);
        tbl.getColumnModel().getColumn(0).setPreferredWidth(60);
        tbl.getColumnModel().getColumn(1).setPreferredWidth(60);
        tbl.getColumnModel().getColumn(2).setPreferredWidth(65);
        tbl.getColumnModel().getColumn(3).setPreferredWidth(50);
        marketTable = tbl;

        // [CONNECT] Replace instruments[] / prices[] with your real Instrumen list
        //   for (Instrumen i : allInstruments) {
        //       marketModel.addRow(new Object[]{i.getKodeInstrumen(), i.getClass().getSimpleName(),
        //                                       i.getHargaSekarang(), "0.00%"});
        //   }
        refreshMarketTable();

        tbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tbl.getSelectedRow() >= 0) {
                selectedInstrument = tbl.getSelectedRow();
                switchInstrument(selectedInstrument);
            }
        });

        JScrollPane sp = new JScrollPane(tbl);
        styleScrollPane(sp);
        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }

    // ── Chart Area ───────────────────────────────────────────────
    JPanel buildChartArea() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 4));
        wrapper.setBackground(BG_DARK);

        // ticker header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_PANEL);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1,1,0,1,BORDER_COLOR),
            BorderFactory.createEmptyBorder(8,12,8,12)));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);
        lblCurrentTicker = new JLabel(instruments[selectedInstrument]);
        lblCurrentTicker.setFont(FONT_PRICE);
        lblCurrentTicker.setForeground(TEXT_PRIMARY);
        lblCurrentType = new JLabel(instTypes[selectedInstrument]);
        lblCurrentType.setFont(FONT_SMALL);
        lblCurrentType.setForeground(ACCENT_BLUE);
        lblCurrentType.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_BLUE),
            BorderFactory.createEmptyBorder(1,5,1,5)));
        left.add(lblCurrentTicker);
        left.add(lblCurrentType);
        header.add(left, BorderLayout.WEST);

        lblCurrentPrice = new JLabel(formatPrice(prices[selectedInstrument]));
        lblCurrentPrice.setFont(FONT_PRICE);
        lblCurrentPrice.setForeground(ACCENT_GREEN);
        lblCurrentPrice.setBorder(BorderFactory.createEmptyBorder(0,0,0,8));
        header.add(lblCurrentPrice, BorderLayout.EAST);

        wrapper.add(header, BorderLayout.NORTH);

        // chart
        chartPanel = new ChartPanel();
        chartPanel.setData(chartData);
        wrapper.add(chartPanel, BorderLayout.CENTER);
        return wrapper;
    }

    // ── Order Panel (Bottom Middle) ──────────────────────────────
    JPanel buildOrderPanel() {
        JPanel panel = darkPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(0, 160));
        panel.setBorder(compoundBorder("NEW ORDER"));

        JPanel fields = new JPanel(new GridLayout(2, 6, 8, 6));
        fields.setOpaque(false);
        fields.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        // Instrument selector
        fields.add(label("Instrument"));
        cmbInstrument = new JComboBox<>(instruments);
        styleCombo(cmbInstrument);
        // [CONNECT] populate with real instrument names from your arrays/lists
        cmbInstrument.addActionListener(e -> {
            selectedInstrument = cmbInstrument.getSelectedIndex();
            switchInstrument(selectedInstrument);
        });
        fields.add(cmbInstrument);

        // Volume
        fields.add(label("Volume (lot)"));
        tfVolume = darkField("1");
        fields.add(tfVolume);

        // Leverage
        fields.add(label("Leverage"));
        tfLeverage = darkField("1");
        fields.add(tfLeverage);

        // SL
        fields.add(label("Stop Loss"));
        tfSL = darkField("0");
        fields.add(tfSL);

        // TP
        fields.add(label("Take Profit"));
        tfTP = darkField("0");
        fields.add(tfTP);

        // Spacer
        fields.add(new JLabel(""));
        fields.add(new JLabel(""));

        panel.add(fields, BorderLayout.CENTER);

        // Buy / Sell buttons
        JPanel btns = new JPanel(new GridLayout(1, 2, 6, 0));
        btns.setOpaque(false);
        btns.setBorder(BorderFactory.createEmptyBorder(0,8,8,8));

        JButton btnBuy = makeOrderBtn("▲  BUY", ACCENT_GREEN);
        JButton btnSell = makeOrderBtn("▼  SELL", ACCENT_RED);

        btnBuy.addActionListener(e -> handleOrder("LONG"));
        btnSell.addActionListener(e -> handleOrder("SHORT"));

        btns.add(btnBuy);
        btns.add(btnSell);
        panel.add(btns, BorderLayout.SOUTH);
        return panel;
    }

    // ── Portfolio Panel (Right) ──────────────────────────────────
    JPanel buildPortfolioPanel() {
        JPanel panel = darkPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(460, 0));
        panel.setBorder(compoundBorder("OPEN POSITIONS"));

        String[] cols = {"Symbol","Pos","Lots","Open","Current","P&L","Gain%","Lev"};
        portfolioModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c){ return false; }
        };
        JTable tbl = new JTable(portfolioModel);
        styleTable(tbl);
        tbl.setDefaultRenderer(Object.class, new PnLCellRenderer());
        tbl.setRowHeight(26);
        portfolioTable = tbl;

        // [CONNECT] Replace this dummy data with real portfolio rows.
        // After every beli() or jual(), call refreshPortfolio()
        // which reads from currentInvestor.getPorto().getDaftarAset()
        addDummyPortfolioRows();

        JScrollPane sp = new JScrollPane(tbl);
        styleScrollPane(sp);
        panel.add(sp, BorderLayout.CENTER);

        // Close selected button
        JButton btnClose = new JButton("Close Selected Position");
        btnClose.setBackground(new Color(0x3A, 0x20, 0x25));
        btnClose.setForeground(ACCENT_RED);
        btnClose.setFont(FONT_BOLD);
        btnClose.setFocusPainted(false);
        btnClose.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1,0,0,0,BORDER_COLOR),
            BorderFactory.createEmptyBorder(8,0,8,0)));
        btnClose.addActionListener(e -> handleClosePosition(tbl.getSelectedRow()));
        panel.add(btnClose, BorderLayout.SOUTH);
        return panel;
    }

    // ── Status Bar ───────────────────────────────────────────────
    JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG_PANEL);
        bar.setBorder(BorderFactory.createMatteBorder(1,0,0,0,BORDER_COLOR));
        bar.setPreferredSize(new Dimension(0, 24));
        lblStatus = new JLabel("  Ready — JRC Broker v1.0");
        lblStatus.setFont(FONT_SMALL);
        lblStatus.setForeground(TEXT_MUTED);
        bar.add(lblStatus, BorderLayout.WEST);

        JLabel time = new JLabel("Market: OPEN   ");
        time.setFont(FONT_SMALL);
        time.setForeground(ACCENT_GREEN);
        bar.add(time, BorderLayout.EAST);
        return bar;
    }

    // ════════════════════════════════════════════════════════════
    //  ACTIONS — wire these to your backend
    // ════════════════════════════════════════════════════════════

    void handleOrder(String posisi) {
        // ── Parse inputs ──
        String ticker = instruments[selectedInstrument];
        double volume, leverage, sl, tp;
        try {
            volume   = Double.parseDouble(tfVolume.getText().trim());
            leverage = Double.parseDouble(tfLeverage.getText().trim());
            sl       = Double.parseDouble(tfSL.getText().trim());
            tp       = Double.parseDouble(tfTP.getText().trim());
        } catch (NumberFormatException ex) {
            setStatus("Invalid input — check Volume / Leverage / SL / TP", ACCENT_RED);
            return;
        }

        // ════════════════════════════════════════════════════════
        // [CONNECT] — Call your beli() method here.
        //
        //  Step 1: Find the Instrumen object matching `ticker`.
        //          You probably have an array or map. Example:
        //
        //    Instrumen found = null;
        //    for (Instrumen ins : allInstruments) {
        //        if (ins.getKodeInstrumen().equals(ticker)) { found = ins; break; }
        //    }
        //
        //  Step 2: Generate a transaction ID (simple counter is fine):
        //    String txId = "TRX" + System.currentTimeMillis();
        //
        //  Step 3: Call beli or handle short:
        //    currentInvestor.beli(txId, found, volume, leverage, posisi);
        //
        //  Step 4: If SL/TP set, apply them:
        //    Kepemilikan k = currentInvestor.getPorto()
        //                       .findKepemilikan(ticker, posisi);
        //    if (k != null && (sl != 0 || tp != 0))
        //        k.setRiskManagement(sl, tp);
        //
        //  Step 5: Refresh the GUI:
        //    refreshPortfolio();
        //    updateAccountStrip();
        // ════════════════════════════════════════════════════════

        // Demo: add a fake row to portfolio table
        double price = prices[selectedInstrument];
        portfolioModel.addRow(new Object[]{
            ticker, posisi,
            df2.format(volume),
            df2.format(price),
            df2.format(price),
            "0.00",
            "0.00%",
            "x" + (int)leverage
        });
        setStatus("Order executed: " + posisi + " " + volume + " lot " + ticker
                  + " @ " + df2.format(price), ACCENT_GREEN);
    }

    void handleClosePosition(int row) {
        if (row < 0) { setStatus("Select a position to close", ACCENT_RED); return; }
        String ticker = portfolioModel.getValueAt(row, 0).toString();
        String posisi = portfolioModel.getValueAt(row, 1).toString();

        // ════════════════════════════════════════════════════════
        // [CONNECT] — Call your jual() method here.
        //
        //  Step 1: Find the Instrumen:
        //    Instrumen found = findInstrument(ticker);
        //
        //  Step 2: Get current holding:
        //    double holding = currentInvestor.getPorto().getHolding(ticker, posisi);
        //
        //  Step 3: Sell all:
        //    String txId = "CLOSE" + System.currentTimeMillis();
        //    currentInvestor.jual(txId, found, holding, posisi);
        //
        //  Step 4: Refresh GUI:
        //    refreshPortfolio();
        //    updateAccountStrip();
        // ════════════════════════════════════════════════════════

        portfolioModel.removeRow(row);
        setStatus("Position closed: " + ticker + " " + posisi, TEXT_MUTED);
    }

    // ════════════════════════════════════════════════════════════
    //  REFRESH HELPERS — call these after backend state changes
    // ════════════════════════════════════════════════════════════

    /**
     * [CONNECT] Call this after any beli() or jual().
     * Replace the body with real data from your Portofolio.
     *
     * Example body:
     *   portfolioModel.setRowCount(0);
     *   for (Kepemilikan k : currentInvestor.getPorto().getDaftarAset()) {
     *       String sym   = k.getInstrumen().getKodeInstrumen();
     *       String pos   = k.getPosisi();
     *       double lots  = k.getUnit();
     *       double open  = k.gethargaBeli();
     *       double cur   = k.getInstrumen().getHargaSekarang();
     *       double pnl   = k.getPnL();
     *       double cap   = (open * lots) / k.getLeverage();
     *       double gain  = cap == 0 ? 0 : (pnl / cap) * 100;
     *       portfolioModel.addRow(new Object[]{
     *           sym, pos, df2.format(lots), df2.format(open),
     *           df2.format(cur), df2.format(pnl),
     *           String.format("%.2f%%", gain),
     *           "x" + (int)k.getLeverage()
     *       });
     *   }
     */
    void refreshPortfolio() {
        // stub — replace with code above
    }

    /**
     * [CONNECT] Call this after every equity/balance update.
     * Replace with real values from your Investor object.
     *
     * Example body:
     *   currentInvestor.updateEquity();   // make sure equity is current
     *   lblBalance.setText(df2.format(currentInvestor.getBalance()));
     *   lblEquity.setText(df2.format(currentInvestor.getEquity()));
     *   lblFreeMargin.setText(df2.format(currentInvestor.getFreeMargin()));
     *   lblUsedMargin.setText(df2.format(currentInvestor.getUsedMargin()));
     *
     * Note: You'll need to add getters for balance, equity, freeMargin,
     *       and usedMargin to your Investor class (they're private right now).
     */
    void updateAccountStrip() {
        lblBalance.setText(df2.format(balance));
        lblEquity.setText(df2.format(equity));
        lblFreeMargin.setText(df2.format(freeMargin));
        lblUsedMargin.setText(df2.format(usedMargin));
    }

    /**
     * [CONNECT] Call this after prices update (inside your simulasiBeberapa loop).
     * Replace prices[] array with real getHargaSekarang() calls.
     *
     * Example body:
     *   marketModel.setRowCount(0);
     *   for (int i = 0; i < allInstruments.length; i++) {
     *       double cur  = allInstruments[i].getHargaSekarang();
     *       double prev = allInstruments[i].getHistoriHarga()
     *                         .get(allInstruments[i].getHistoriHarga().size()-2); // previous price
     *       double chg  = ((cur - prev) / prev) * 100;
     *       marketModel.addRow(new Object[]{
     *           allInstruments[i].getKodeInstrumen(),
     *           allInstruments[i].getClass().getSimpleName(),
     *           formatPrice(cur),
     *           String.format("%+.2f%%", chg)
     *       });
     *   }
     */
    void refreshMarketTable() {
        marketModel.setRowCount(0);
        for (int i = 0; i < instruments.length; i++) {
            double chg = prevPrices[i] == 0 ? 0
                         : ((prices[i] - prevPrices[i]) / prevPrices[i]) * 100;
            marketModel.addRow(new Object[]{
                instruments[i], instTypes[i],
                formatPrice(prices[i]),
                String.format("%+.2f%%", chg)
            });
        }
    }

    // ════════════════════════════════════════════════════════════
    //  MARKET SIMULATION — Replace with your simulasiBeberapa()
    // ════════════════════════════════════════════════════════════

    /**
     * [CONNECT] This timer simulates price changes.
     * Replace it by running your simulasiBeberapa() on a background thread
     * and calling SwingUtilities.invokeLater(...) for each GUI update.
     *
     * Example — to hook real simulation:
     *   Thread simThread = new Thread(() -> {
     *       // after each price step in simulasiBeberapa:
     *       SwingUtilities.invokeLater(() -> {
     *           prices[i] = allInstruments[i].getHargaSekarang();
     *           refreshMarketTable();
     *           refreshPortfolio();
     *           updateAccountStrip();
     *           updateChart(selectedInstrument);
     *       });
     *   });
     *   simThread.start();
     */
    void startMarketSimulation() {
        Timer timer = new Timer(1200, e -> {
            prevPrices = prices.clone();
            for (int i = 0; i < prices.length; i++) {
                double pct = (rng.nextDouble() * 0.014 - 0.007);
                prices[i] = Math.max(0.001, prices[i] * (1 + pct));
            }
            refreshMarketTable();
            updateChart(selectedInstrument);
            // update header price
            lblCurrentPrice.setText(formatPrice(prices[selectedInstrument]));
            double chg = (prices[selectedInstrument] - prevPrices[selectedInstrument])
                        / prevPrices[selectedInstrument];
            lblCurrentPrice.setForeground(chg >= 0 ? ACCENT_GREEN : ACCENT_RED);
        });
        timer.start();
    }

    void updateChart(int idx) {
        if (chartData.size() > 200) chartData.remove(0);
        chartData.add(prices[idx]);
        chartPanel.setData(chartData);
    }

    void switchInstrument(int idx) {
        lblCurrentTicker.setText(instruments[idx]);
        lblCurrentType.setText(instTypes[idx]);
        lblCurrentPrice.setText(formatPrice(prices[idx]));
        cmbInstrument.setSelectedIndex(idx);
        chartData.clear();
        double seed = prices[idx];
        for (int i = 0; i < 80; i++) {
            seed *= 1 + (rng.nextDouble() * 0.02 - 0.01);
            chartData.add(seed);
        }
        chartPanel.setData(chartData);
    }

    // ════════════════════════════════════════════════════════════
    //  UTILITY METHODS
    // ════════════════════════════════════════════════════════════

    void setStatus(String msg, Color color) {
        lblStatus.setText("  " + msg);
        lblStatus.setForeground(color);
    }

    String formatPrice(double p) {
        return p < 10 ? df5.format(p) : df2.format(p);
    }

    JPanel darkPanel() {
        JPanel p = new JPanel();
        p.setBackground(BG_PANEL);
        return p;
    }

    JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL);
        l.setForeground(TEXT_MUTED);
        return l;
    }

    JTextField darkField(String val) {
        JTextField tf = new JTextField(val);
        tf.setBackground(BG_WIDGET);
        tf.setForeground(TEXT_PRIMARY);
        tf.setFont(FONT_MONO);
        tf.setCaretColor(ACCENT_BLUE);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(3,6,3,6)));
        return tf;
    }

    void styleCombo(JComboBox<?> cb) {
        cb.setBackground(BG_WIDGET);
        cb.setForeground(TEXT_PRIMARY);
        cb.setFont(FONT_MONO);
    }

    void styleTable(JTable tbl) {
        tbl.setBackground(BG_PANEL);
        tbl.setForeground(TEXT_PRIMARY);
        tbl.setFont(FONT_LABEL);
        tbl.setGridColor(BORDER_COLOR);
        tbl.setSelectionBackground(ACCENT_BLUE.darker());
        tbl.setSelectionForeground(Color.WHITE);
        tbl.setShowVerticalLines(false);
        tbl.setIntercellSpacing(new Dimension(0, 1));
        tbl.getTableHeader().setBackground(BG_WIDGET);
        tbl.getTableHeader().setForeground(TEXT_MUTED);
        tbl.getTableHeader().setFont(FONT_SMALL);
        tbl.getTableHeader().setBorder(BorderFactory.createMatteBorder(0,0,1,0,BORDER_COLOR));
    }

    void styleScrollPane(JScrollPane sp) {
        sp.setBackground(BG_PANEL);
        sp.getViewport().setBackground(BG_PANEL);
        sp.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
    }

    JButton makeOrderBtn(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color.darker().darker());
        btn.setForeground(color);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(color));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); btn.setForeground(Color.WHITE); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color.darker().darker()); btn.setForeground(color); }
        });
        return btn;
    }

    Border compoundBorder(String title) {
        TitledBorder tb = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR), " " + title + " ");
        tb.setTitleFont(FONT_SMALL);
        tb.setTitleColor(TEXT_MUTED);
        return BorderFactory.createCompoundBorder(tb, BorderFactory.createEmptyBorder(2,2,2,2));
    }

    void addDummyPortfolioRows() {
        // [CONNECT] Remove this and call refreshPortfolio() instead
        portfolioModel.addRow(new Object[]{"BBCA","LONG","10.00","9500.00","9500.00","0.00","0.00%","x1"});
        portfolioModel.addRow(new Object[]{"Gold","SHORT","0.50","4500.00","4500.00","0.00","0.00%","x5"});
    }

    // ════════════════════════════════════════════════════════════
    //  CUSTOM CHART COMPONENT
    // ════════════════════════════════════════════════════════════
    static class ChartPanel extends JPanel {
        private ArrayList<Double> data = new ArrayList<>();

        void setData(ArrayList<Double> d) {
            this.data = new ArrayList<>(d);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            g.setColor(BG_PANEL);
            g.fillRect(0, 0, w, h);

            // grid lines
            g.setColor(BORDER_COLOR);
            g.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                                         0, new float[]{4}, 0));
            int gridLines = 6;
            for (int i = 0; i <= gridLines; i++) {
                int y = (int)(h * i / (double)gridLines);
                g.drawLine(0, y, w, y);
            }
            int vLines = 8;
            for (int i = 0; i <= vLines; i++) {
                int x = (int)(w * i / (double)vLines);
                g.drawLine(x, 0, x, h);
            }

            if (data.size() < 2) return;

            // find range
            double mn = data.stream().mapToDouble(Double::doubleValue).min().orElse(0);
            double mx = data.stream().mapToDouble(Double::doubleValue).max().orElse(1);
            double range = mx - mn;
            if (range == 0) range = 1;
            double pad = range * 0.1;
            mn -= pad; mx += pad; range = mx - mn;

            int n = data.size();
            double stepX = (double)w / (n - 1);
            int pad2 = 30;

            // build path
            int[] xs = new int[n], ys = new int[n];
            for (int i = 0; i < n; i++) {
                xs[i] = (int)(i * stepX);
                ys[i] = (int)(pad2 + (mx - data.get(i)) / range * (h - pad2 * 2));
            }

            // area fill
            boolean up = data.get(n-1) >= data.get(0);
            Color lineColor = up ? ACCENT_GREEN : ACCENT_RED;
            Color fillColor = up ? new Color(0, 212, 126, 30) : new Color(255, 69, 90, 30);

            GeneralPath area = new GeneralPath();
            area.moveTo(xs[0], h);
            for (int i = 0; i < n; i++) area.lineTo(xs[i], ys[i]);
            area.lineTo(xs[n-1], h);
            area.closePath();
            g.setColor(fillColor);
            g.fill(area);

            // line
            GeneralPath line = new GeneralPath();
            line.moveTo(xs[0], ys[0]);
            for (int i = 1; i < n; i++) line.lineTo(xs[i], ys[i]);
            g.setColor(lineColor);
            g.setStroke(new BasicStroke(2f));
            g.draw(line);

            // price labels on Y axis
            g.setFont(FONT_SMALL);
            g.setColor(TEXT_MUTED);
            g.setStroke(new BasicStroke(1));
            DecimalFormat lf = new DecimalFormat("#,##0.00");
            for (int i = 0; i <= 4; i++) {
                double val = mn + (mx - mn) * i / 4.0;
                int y = (int)(pad2 + (mx - val) / range * (h - pad2 * 2));
                g.drawString(lf.format(val), 4, y - 2);
            }

            // current price line
            double lastP = data.get(n-1);
            int lastY = ys[n-1];
            g.setColor(lineColor);
            g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                                         0, new float[]{6, 3}, 0));
            g.drawLine(0, lastY, w, lastY);
            g.setColor(lineColor);
            g.fillRect(w - 80, lastY - 9, 76, 17);
            g.setColor(BG_DARK);
            g.setFont(FONT_MONO);
            g.drawString(lf.format(lastP), w - 76, lastY + 5);
        }
    }

    // ════════════════════════════════════════════════════════════
    //  CUSTOM CELL RENDERERS
    // ════════════════════════════════════════════════════════════
    static class PnLCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(
            JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, v, sel, foc, r, c);
            setBackground(r % 2 == 0 ? BG_PANEL : BG_ROW_ALT);
            setForeground(TEXT_PRIMARY);
            setBorder(BorderFactory.createEmptyBorder(0,6,0,6));
            if (c == 5 || c == 6) {
                String s = v == null ? "0" : v.toString().replace("%","").replace(",","");
                try {
                    double val = Double.parseDouble(s);
                    setForeground(val >= 0 ? ACCENT_GREEN : ACCENT_RED);
                } catch (NumberFormatException ignored) {}
            }
            if (c == 1) {
                String s = v == null ? "" : v.toString();
                setForeground(s.equals("LONG") ? ACCENT_GREEN : ACCENT_RED);
            }
            if (sel) { setBackground(ACCENT_BLUE.darker()); setForeground(Color.WHITE); }
            return this;
        }
    }

    static class MarketCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(
            JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, v, sel, foc, r, c);
            setBackground(r % 2 == 0 ? BG_PANEL : BG_ROW_ALT);
            setForeground(TEXT_PRIMARY);
            setBorder(BorderFactory.createEmptyBorder(0,4,0,4));
            if (c == 3 && v != null) {
                try {
                    double val = Double.parseDouble(v.toString().replace("%","").replace("+",""));
                    setForeground(val >= 0 ? ACCENT_GREEN : ACCENT_RED);
                } catch (NumberFormatException ignored) {}
            }
            if (c == 1) { setForeground(ACCENT_BLUE); setFont(FONT_SMALL); }
            if (sel) { setBackground(ACCENT_BLUE.darker()); setForeground(Color.WHITE); }
            return this;
        }
    }
}
