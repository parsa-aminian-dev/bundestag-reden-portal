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

        section:hover {
            transform: translateY(-2px);
            box-shadow: 0 12px 40px rgba(0,0,0,0.12);
        }

        h2 {
            color: #2c3e50;
            margin-bottom: 1.5rem;
            font-size: 1.8rem;
            font-weight: 600;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .search-box {
            position: relative;
        }

        #searchInput {
            width: 100%;
            padding: 1rem 1.5rem;
            font-size: 1.1rem;
            border: 2px solid #e0e6ed;
            border-radius: 12px;
            transition: all 0.3s ease;
            background: #f8f9fa;
        }

        #searchInput:focus {
            outline: none;
            border-color: #3498db;
            background: white;
            box-shadow: 0 0 0 4px rgba(52, 152, 219, 0.1);
        }

        .search-results {
            position: absolute;
            top: calc(100% + 10px);
            left: 0;
            right: 0;
            background: white;
            border-radius: 12px;
            box-shadow: 0 8px 30px rgba(0,0,0,0.15);
            max-height: 400px;
            overflow-y: auto;
            z-index: 100;
        }

        .filter-form {
            display: flex;
            gap: 2rem;
            align-items: flex-end;
            flex-wrap: wrap;
        }

        .filter-group {
            flex: 1;
            min-width: 200px;
            display: flex;
            flex-direction: column;
            gap: 0.5rem;
        }

        .filter-group label {
            font-weight: 600;
            color: #34495e;
            font-size: 0.95rem;
        }

        select {
            padding: 0.75rem 1rem;
            border: 2px solid #e0e6ed;
            border-radius: 10px;
            font-size: 1rem;
            background: #f8f9fa;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        select:hover {
            border-color: #3498db;
        }

        select:focus {
            outline: none;
            border-color: #3498db;
            background: white;
            box-shadow: 0 0 0 4px rgba(52, 152, 219, 0.1);
        }

        button {
            padding: 0.75rem 1.5rem;
            border: none;
            border-radius: 10px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        button:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
        }

        button[type="reset"] {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }

        .abgeordnete-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
            gap: 2rem;
        }

        .abgeordneter-card {
            background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
            border-radius: 16px;
            overflow: hidden;
            transition: all 0.3s ease;
            border: 2px solid #e0e6ed;
            display: flex;
            flex-direction: column;
        }

        .abgeordneter-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 12px 40px rgba(0,0,0,0.15);
            border-color: #3498db;
        }

        .card-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 1.5rem;
            color: white;
        }

        .card-header h3 {
            margin: 0;
            font-size: 1.3rem;
        }

        .card-header h3 a {
            color: white;
            text-decoration: none;
            transition: opacity 0.3s ease;
        }

        .card-header h3 a:hover {
            opacity: 0.9;
        }

        .card-body {
            padding: 1.5rem;
            flex: 1;
        }

        .card-body p {
            margin-bottom: 1rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .badge {
            display: inline-block;
            padding: 0.4rem 0.8rem;
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
            border-radius: 20px;
            font-size: 0.85rem;
            font-weight: 600;
        }

        .count {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 40px;
            height: 40px;
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            color: white;
            border-radius: 50%;
            font-weight: 700;
            font-size: 1.1rem;
        }

        .card-footer {
            padding: 1.5rem;
            background: #f8f9fa;
            border-top: 1px solid #e0e6ed;
        }

        .btn {
            display: inline-block;
            padding: 0.75rem 1.5rem;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            text-decoration: none;
            border-radius: 10px;
            font-weight: 600;
            transition: all 0.3s ease;
            text-align: center;
            width: 100%;
        }

        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
        }

        .no-results {
            text-align: center;
            padding: 3rem;
            color: #7f8c8d;
            font-size: 1.2rem;
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
            }

            .filter-form {
                flex-direction: column;
                align-items: stretch;
            }

            .abgeordnete-grid {
                grid-template-columns: 1fr;
            }

            section {
                padding: 1.5rem;
            }
        }

        /* Animationen */
        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .abgeordneter-card {
            animation: fadeIn 0.5s ease forwards;
        }

        .abgeordneter-card:nth-child(1) { animation-delay: 0.1s; }
        .abgeordneter-card:nth-child(2) { animation-delay: 0.2s; }
        .abgeordneter-card:nth-child(3) { animation-delay: 0.3s; }
    </style>
</head>
<body>
<header>
    <div class="container">
        <div class="header-top">
            <img src="/static/img/deutscher-bundestag-seeklogo.png" alt="Deutscher Bundestag" class="bundestag-logo">
            <nav>
                <a href="/">Home</a>
                <a href="/statistiken">Statistiken</a>
                <a href="/api/abgeordnete" target="_blank">API</a>
            </nav>
        </div>
    </div>
</header>

<main class="container">
    <!-- Suchbereich -->
    <section class="search-section">
        <h2>🔍 Schnelle Suche</h2>
        <div class="search-box">
            <input type="text"
                   id="searchInput"
                   placeholder="Nach Name oder ID suchen..."
                   autocomplete="off">
            <div id="searchResults" class="search-results"></div>
        </div>
    </section>

    <!-- Filter und Sortierung -->
    <section class="filter-section">
        <h2>🔧 Filter & Optionen</h2>
        <form method="get" action="/" class="filter-form">
            <div class="filter-group">
                <label for="fraktionFilter">📋 Fraktion:</label>
                <select name="fraktion" id="fraktionFilter" onchange="this.form.submit()">
                    <option value="">Alle Fraktionen</option>
                    <#list fraktionen as fraktion>
                        <option value="${fraktion.name}"
                                <#if selectedFraktion?? && selectedFraktion == fraktion.name>selected</#if>>
                            ${fraktion.name}
                        </option>
                    </#list>
                </select>
            </div>

            <div class="filter-group">
                <label for="sortBy">📊 Sortieren:</label>
                <select name="sort" id="sortBy" onchange="this.form.submit()">
                    <option value="name" <#if sortBy?? && sortBy == "name">selected</#if>>Name (A-Z)</option>
                    <option value="reden" <#if sortBy?? && sortBy == "reden">selected</#if>>Anzahl Reden</option>
                </select>
            </div>

            <button type="reset" onclick="window.location.href='/'">↺ Zurücksetzen</button>
        </form>
    </section>

    <!-- Abgeordneten-Liste -->
    <section class="abgeordnete-section">
        <h2>👥 Abgeordnete (${abgeordnete?size})</h2>

        <#if abgeordnete?size == 0>
            <p class="no-results">Keine Abgeordneten gefunden.</p>
        <#else>
            <div class="abgeordnete-grid">
                <#list abgeordnete as abg>
                    <div class="abgeordneter-card">
                        <div class="card-header">
                            <h3>
                                <a href="/abgeordneter/${abg.id}">
                                    ${abg.vorname} ${abg.nachname}
                                </a>
                            </h3>
                        </div>
                        <div class="card-body">
                            <p class="fraktion">
                                <strong>Fraktion:</strong>
                                <span class="badge">${abg.fraktion.name}</span>
                            </p>
                            <p class="reden-count">
                                <strong>Anzahl Reden:</strong>
                                <span class="count">${abg.reden?size}</span>
                            </p>
                        </div>
                        <div class="card-footer">
                            <a href="/abgeordneter/${abg.id}" class="btn">Details ansehen →</a>
                        </div>
                    </div>
                </#list>
            </div>
        </#if>
    </section>
</main>

<footer>
    <div class="container">
        <p>&copy; 2025 Bundestag Reden-Portal | Version ${version} | Goethe Universität Frankfurt</p>
    </div>
</footer>

<script src="/static/js/main.js"></script>
</body>
</html>