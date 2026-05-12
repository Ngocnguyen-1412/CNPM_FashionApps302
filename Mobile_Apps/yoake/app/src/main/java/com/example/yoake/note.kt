// ============================================================
//  REQUIRED ADDITIONS — paste these into the right files
// ============================================================


// ── 1. app/build.gradle.kts  (dependencies block) ─────────────────────────────

dependencies {

    // Retrofit + Gson (REST ↔ MySQL layer)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // OkHttp logging (remove in production)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // ViewModel + StateFlow lifecycle helpers
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.3")

    // (Already in project — confirm present)
    implementation("io.coil-kt:coil-compose:2.6.0")
}


// ── 2. AndroidManifest.xml  (inside <manifest> tag, before <application>) ─────

/*
    <uses-permission android:name="android.permission.INTERNET" />
*/

// If your dev server is plain HTTP (not HTTPS), also add inside <application>:
/*
    android:usesCleartextTraffic="true"
*/

// Or create res/xml/network_security_config.xml and reference it:
/*
    android:networkSecurityConfig="@xml/network_security_config"
*/

// network_security_config.xml content for local dev:
/*
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">10.0.2.2</domain>
        <domain includeSubdomains="true">192.168.1.0</domain>
    </domain-config>
</network-security-config>
*/


// ── 3. Minimal Node.js / Express back-end reference ──────────────────────────
//
//  If you need a quick back-end to serve Retrofit, here is a minimal
//  Express + mysql2 server that maps the Android API calls to the SQL views.
//
//  npm install express mysql2
//
/*
const express = require('express');
const mysql   = require('mysql2/promise');
const app     = express();
app.use(express.json());

const pool = mysql.createPool({
  host:     'localhost',
  user:     'root',
  password: 'your_password',
  database: 'fashion_apps',
});

// GET /home?user_id=1
app.get('/home', async (req, res) => {
  const [cats]  = await pool.query('SELECT name FROM Categories');
  const [prods] = await pool.query(`
    SELECT p.*, GROUP_CONCAT(DISTINCT pi.url ORDER BY pi.sort_order) AS image_urls
    FROM v_product_detail p
    LEFT JOIN Product_Images pi ON pi.product_id = p.id
    GROUP BY p.id`);
  // build HomeDto and return it
  res.json({ categories: cats.map(r => r.name), featured_product: prods[0] ?? null,
             grid_products: prods.slice(1), curated_title: 'New Arrivals',
             curated_body:  'Thoughtfully crafted pieces for every occasion.',
             curated_image_url: prods[0]?.image_urls?.split(',')[0] ?? '' });
});

// GET /products/:id
app.get('/products/:id', async (req, res) => {
  const [[prod]] = await pool.query(
    'SELECT * FROM v_product_detail WHERE id = ?', [req.params.id]);
  if (!prod) return res.status(404).json({ error: 'Not found' });
  const [images] = await pool.query(
    'SELECT url FROM Product_Images WHERE product_id = ? ORDER BY sort_order', [prod.id]);
  const [colors] = await pool.query(
    'SELECT name, hex FROM Product_Colors WHERE product_id = ?', [prod.id]);
  const [sizes]  = await pool.query(
    'SELECT size_label FROM Product_Sizes WHERE product_id = ?', [prod.id]);
  res.json({ ...prod, image_urls: images.map(r => r.url),
             colors: colors, sizes: sizes.map(r => r.size_label) });
});

// GET /orders?user_id=1&page=1&limit=10
app.get('/orders', async (req, res) => {
  const { user_id, page = 1, limit = 10 } = req.query;
  const offset = (page - 1) * limit;
  const [[{ total_count }]] = await pool.query(
    'SELECT COUNT(*) AS total_count FROM v_order_history WHERE user_id = ?', [user_id]);
  const [orders] = await pool.query(
    'SELECT * FROM v_order_history WHERE user_id = ? LIMIT ? OFFSET ?',
    [user_id, +limit, +offset]);
  res.json({ orders, total_count });
});

// POST /cart/add
app.post('/cart/add', async (req, res) => {
  const { user_id, product_id, quantity, selected_color, selected_size } = req.body;
  // Find or create a Cart order for this user
  let [[cart]] = await pool.query(
    "SELECT order_id FROM Orders WHERE user_id = ? AND status = 'Cart'", [user_id]);
  if (!cart) {
    const [result] = await pool.query(
      "INSERT INTO Orders (user_id, total_amount, status) VALUES (?, 0, 'Cart')", [user_id]);
    cart = { order_id: result.insertId };
  }
  const [[product]] = await pool.query(
    'SELECT price FROM Products WHERE product_id = ?', [product_id]);
  await pool.query(
    `INSERT INTO Order_Items (order_id, product_id, quantity, price, selected_color, selected_size)
     VALUES (?, ?, ?, ?, ?, ?)`,
    [cart.order_id, product_id, quantity, product.price, selected_color, selected_size]);
  // Update total
  await pool.query(
    'UPDATE Orders SET total_amount = (SELECT SUM(price * quantity) FROM Order_Items WHERE order_id = ?) WHERE order_id = ?',
    [cart.order_id, cart.order_id]);
  res.json({ success: true, order_id: cart.order_id });
});

// GET /users/:id
app.get('/users/:id', async (req, res) => {
  const [[user]] = await pool.query(
    'SELECT user_id, display_name, email, avatar_url FROM Users WHERE user_id = ?',
    [req.params.id]);
  if (!user) return res.status(404).json({ error: 'Not found' });
  res.json(user);
});

app.listen(3000, () => console.log('Yoake API running on :3000'));
*/
