<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <link rel="stylesheet" href="/static/css/style.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
            background: linear-gradient(135deg, #f5f7fa 0%, #e8edf2 100%);
            min-height: 100vh;
            color: #2c3e50;
            line-height: 1.6;
        }

        header {
            background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            position: sticky;
            top: 0;
            z-index: 1000;
        }

        .container {
            max-width: 1400px;
            margin: 0 auto;
            padding: 0 20px;
        }

        .header-top {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 1.5rem 0;
        }

        .bundestag-logo {
            height: 50px;
            filter: brightness(0) invert(1);
            transition: transform 0.3s ease;
        }

        .bundestag-logo:hover {
            transform: scale(1.05);
        }

        nav {
            display: flex;
            gap: 2rem;
        }

        nav a {
            color: white;
            text-decoration: none;
            font-weight: 500;
            padding: 0.5rem 1rem;
            border-radius: 8px;
            transition: all 0.3s ease;
            position: relative;
        }

        nav a::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 50%;
            width: 0;
            height: 2px;
            background: #3498db;
            transition: all 0.3s ease;
            transform: translateX(-50%);
        }

        nav a:hover {
            background: rgba(52, 152, 219, 0.1);
        }

        nav a:hover::after {
            width: 80%;
        }

        main {
            padding: 3rem 0;
        }

        section {
            background: white;
            border-radius: 16px;
            padding: 2.5rem;
            margin-bottom: 2rem;
            box-shadow: 0 8px 30px rgba(0,0,0,0.08);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        h2 {
            color: #2c3e50;
            margin-bottom: 2rem;
            font-size: 1.8rem;
            font-weight: 600;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        h3 {
            color: #34495e;
            margin-bottom: 1.5rem;
            font-size: 1.4rem;
            font-weight: 600;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        /* Statistik-Karten */
        .stats-overview {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 1.5rem;
            margin-bottom: 3rem;
        }

        .stat-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 16px;
            padding: 2rem;
            text-align: center;
            color: white;
            box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
        }

        .stat-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, transparent 100%);
            opacity: 0;
            transition: opacity 0.3s ease;
        }

        .stat-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 12px 35px rgba(102, 126, 234, 0.4);
        }

        .stat-card:hover::before {
            opacity: 1;
        }

        .stat-card:nth-child(2) {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            box-shadow: 0 8px 25px rgba(240, 147, 251, 0.3);
        }

        .stat-card:nth-child(3) {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            box-shadow: 0 8px 25px rgba(79, 172, 254, 0.3);
        }

        .stat-card:nth-child(4) {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
            box-shadow: 0 8px 25px rgba(67, 233, 123, 0.3);
        }

        .stat-icon {
            font-size: 3rem;
            margin-bottom: 1rem;
        }

        .stat-value {
            font-size: 2.5rem;
            font-weight: 700;
            margin-bottom: 0.5rem;
        }

        .stat-label {
            font-size: 1rem;
            opacity: 0.9;
            font-weight: 500;
        }

        /* Tabellen */
        .top-redner-section,
        .fraktions-stats-section {
            margin-top: 3rem;
        }

        .stats-table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0;
            margin-top: 1.5rem;
            overflow: hidden;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
        }

        .stats-table thead {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .stats-table th {
            padding: 1.2rem 1.5rem;
            text-align: left;
            font-weight: 600;
            font-size: 0.95rem;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .stats-table tbody tr {
            background: white;
            transition: all 0.3s ease;
            border-bottom: 1px solid #e0e6ed;
        }

        .stats-table tbody tr:hover {
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
            transform: scale(1.01);
            box-shadow: 0 4px 12px rgba(0,0,0,0.08);
        }

        .stats-table td {
            padding: 1.2rem 1.5rem;
            color: #2c3e50;
        }

        .stats-table td a {
            color: #667eea;
            text-decoration: none;
            font-weight: 600;
            transition: color 0.3s ease;
        }

        .stats-table td a:hover {
            color: #764ba2;
            text-decoration: underline;
        }

        .rank {
            font-weight: 700;
            font-size: 1.2rem;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .badge {
            display: inline-block;
            padding: 0.4rem 0.9rem;
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
            border-radius: 20px;
            font-size: 0.85rem;
            font-weight: 600;
            box-shadow: 0 2px 8px rgba(240, 147, 251, 0.3);
        }

        .count {
            font-weight: 700;
            color: #667eea;
            font-size: 1.1rem;
        }

        footer {
            background: #1a1a2e;
            color: white;
            padding: 2rem 0;
            margin-top: 4rem;
            text-align: center;
        }

        footer p {
            opacity: 0.8;
        }

        @media (max-width: 768px) {
            .header-top {
                flex-direction: column;
                gap: 1rem;
            }

            nav {
                gap: 1rem;
                flex-wrap: wrap;
                justify-content: center;
            }

            .stats-overview {
                grid-template-columns: 1fr;
            }

            section {
                padding: 1.5rem;
            }

            .stats-table {
                font-size: 0.9rem;
            }

            .stats-table th,
            .stats-table td {
                padding: 0.8rem;
            }
        }

        /* Animationen */
        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .stat-card {
            animation: fadeInUp 0.6s ease forwards;
        }

        .stat-card:nth-child(1) { animation-delay: 0.1s; }
        .stat-card:nth-child(2) { animation-delay: 0.2s; }
        .stat-card:nth-child(3) { animation-delay: 0.3s; }
        .stat-card:nth-child(4) { animation-delay: 0.4s; }
    </style>
</head>
<body>
<header>
    <div class="container">
        <div class="header-top">
            <img src="/static/img/deutscher-bundestag-seeklogo.png" alt="Deutscher Bundestag" class="bundestag-logo">
            <nav>
                <a href="/">← Home</a>
                <a href="/statistiken">Statistiken</a>
            </nav>
        </div>
    </div>
</header>

<main class="container">
    <section class="stats-section">
        <h2>📊 Gesamt-Statistiken</h2>

        <!-- Übersicht-Karten -->
        <div class="stats-overview">
            <div class="stat-card">
                <div class="stat-icon">👥</div>
                <div class="stat-value">${statistiken.gesamtAbgeordnete}</div>
                <div class="stat-label">Abgeordnete</div>
            </div>

            <div class="stat-card">
                <div class="stat-icon">📝</div>
                <div class="stat-value">${statistiken.gesamtReden}</div>
                <div class="stat-label">Reden</div>
            </div>

            <div class="stat-card">
                <div class="stat-icon">🏢</div>
                <div class="stat-value">${statistiken.gesamtFraktionen}</div>
                <div class="stat-label">Fraktionen</div>
            </div>

            <div class="stat-card">
                <div class="stat-icon">📏</div>
                <div class="stat-value">${statistiken.durchschnittRedelaenge}</div>
                <div class="stat-label">Ø Redelänge (Zeichen)</div>
            </div>
        </div>

        <!-- Top-Redner -->
        <div class="top-redner-section">
            <h3>🏆 Top 5 Redner</h3>
            <table class="stats-table">
                <thead>
                <tr>
                    <th>Rang</th>
                    <th>Name</th>
                    <th>Fraktion</th>
                    <th>Anzahl Reden</th>
                </tr>
                </thead>
                <tbody>
                <#list statistiken.topRedner as redner>
                    <tr>
                        <td class="rank">${redner?counter}</td>
                        <td>
                            <a href="/abgeordneter/${redner.id}">
                                ${redner.name}
                            </a>
                        </td>
                        <td>
                            <span class="badge">${redner.fraktion}</span>
                        </td>
                        <td class="count">${redner.redenAnzahl}</td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>

        <!-- Fraktions-Statistiken -->
        <div class="fraktions-stats-section">
            <h3>🏢 Statistiken nach Fraktion</h3>
            <table class="stats-table">
                <thead>
                <tr>
                    <th>Fraktion</th>
                    <th>Mitglieder</th>
                    <th>Gesamt-Reden</th>
                    <th>Ø Reden pro Mitglied</th>
                    <th>Ø Redelänge</th>
                </tr>
                </thead>
                <tbody>
                <#list statistiken.fraktionsStatistiken as fraktion>
                    <tr>
                        <td><span class="badge">${fraktion.name}</span></td>
                        <td class="count">${fraktion.mitgliederAnzahl}</td>
                        <td class="count">${fraktion.gesamtReden}</td>
                        <td>${fraktion.durchschnittRedenProMitglied}</td>
                        <td>${fraktion.durchschnittRedelaenge} Zeichen</td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </section>
</main>

<footer>
    <div class="container">
        <p>&copy; 2025 Bundestag Reden-Portal | Goethe Universität Frankfurt</p>
    </div>
</footer>

<script src="/static/js/main.js"></script>
</body>
</html>