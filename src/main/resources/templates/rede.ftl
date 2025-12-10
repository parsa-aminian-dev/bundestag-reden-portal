<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rede Details - Bundestag Portal</title>
    <link rel="stylesheet" href="/static/css/style.css">
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
            max-width: 1200px;
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
            gap: 1.5rem;
            flex-wrap: wrap;
        }

        nav a {
            color: white;
            text-decoration: none;
            font-weight: 500;
            padding: 0.5rem 1rem;
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        nav a:hover {
            background: rgba(52, 152, 219, 0.1);
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
            animation: fadeInUp 0.5s ease forwards;
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

        /* Rede Meta Card */
        .rede-meta-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 16px;
            padding: 2.5rem;
            color: white;
            box-shadow: 0 12px 40px rgba(102, 126, 234, 0.3);
        }

        .rede-meta-card h2 {
            color: white;
            border-bottom: 2px solid rgba(255,255,255,0.2);
            padding-bottom: 1rem;
        }

        .meta-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 1.5rem;
            margin-top: 1.5rem;
        }

        .meta-item {
            background: rgba(255,255,255,0.1);
            padding: 1.2rem;
            border-radius: 12px;
            backdrop-filter: blur(10px);
            transition: all 0.3s ease;
        }

        .meta-item:hover {
            background: rgba(255,255,255,0.15);
            transform: translateY(-2px);
        }

        .meta-item strong {
            display: block;
            font-size: 0.85rem;
            opacity: 0.9;
            margin-bottom: 0.5rem;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .meta-item span {
            font-size: 1.1rem;
            font-weight: 600;
        }

        .meta-item a {
            color: white;
            text-decoration: none;
            transition: opacity 0.3s ease;
        }

        .meta-item a:hover {
            opacity: 0.8;
            text-decoration: underline;
        }

        .badge {
            display: inline-block;
            padding: 0.5rem 1rem;
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            border-radius: 20px;
            font-size: 0.95rem;
            font-weight: 600;
            box-shadow: 0 2px 8px rgba(240, 147, 251, 0.3);
        }

        .count {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 35px;
            height: 35px;
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            border-radius: 50%;
            font-weight: 700;
            font-size: 1.1rem;
        }

        /* Redetext */
        .rede-text-card {
            background: #fafbfc;
            border-radius: 12px;
            padding: 2.5rem;
            border-left: 4px solid #667eea;
        }

        .rede-text-content p {
            margin-bottom: 1.5rem;
            color: #2c3e50;
            line-height: 1.9;
            font-size: 1.05rem;
            text-align: justify;
        }

        .rede-text-content p:last-child {
            margin-bottom: 0;
        }

        /* Kommentare */
        .kommentare-list {
            display: grid;
            gap: 1rem;
        }

        .kommentar-card {
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
            padding: 1.5rem;
            border-radius: 12px;
            border-left: 4px solid #4facfe;
            transition: all 0.3s ease;
        }

        .kommentar-card:hover {
            transform: translateX(5px);
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }

        .kommentar-card p {
            color: #495057;
            line-height: 1.7;
            font-style: italic;
        }

        /* Aktionsbereich */
        .actions-card {
            display: flex;
            gap: 1.5rem;
            flex-wrap: wrap;
        }

        .btn {
            flex: 1;
            min-width: 200px;
            padding: 1rem 2rem;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            text-decoration: none;
            border-radius: 12px;
            font-weight: 600;
            text-align: center;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
        }

        .btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
        }

        .btn-secondary {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            box-shadow: 0 4px 15px rgba(79, 172, 254, 0.3);
        }

        .btn-secondary:hover {
            box-shadow: 0 8px 25px rgba(79, 172, 254, 0.4);
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
                gap: 0.5rem;
                justify-content: center;
            }

            nav a {
                font-size: 0.9rem;
                padding: 0.4rem 0.8rem;
            }

            .meta-grid {
                grid-template-columns: 1fr;
            }

            section {
                padding: 1.5rem;
            }

            .rede-text-card {
                padding: 1.5rem;
            }

            .actions-card {
                flex-direction: column;
            }

            .btn {
                min-width: 100%;
            }
        }

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

        section:nth-child(1) { animation-delay: 0.1s; }
        section:nth-child(2) { animation-delay: 0.2s; }
        section:nth-child(3) { animation-delay: 0.3s; }
        section:nth-child(4) { animation-delay: 0.4s; }
    </style>
</head>
<body>
<header>
    <div class="container">
        <div class="header-top">
            <img src="/static/img/deutscher-bundestag-seeklogo.png" alt="Deutscher Bundestag" class="bundestag-logo">
            <nav>
                <a href="/">← Home</a>
                <a href="/abgeordneter/${redner.id}">← Zurück zu ${redner.vorname} ${redner.nachname}</a>
            </nav>
        </div>
    </div>
</header>

<main class="container">
    <!-- Rede-Metadaten -->
    <section class="rede-meta-section">
        <div class="rede-meta-card">
            <h2>📄 Rede-Informationen</h2>
            <div class="meta-grid">
                <div class="meta-item">
                    <strong>Redner:</strong>
                    <span>
                        <a href="/abgeordneter/${redner.id}">
                            ${redner.vorname} ${redner.nachname}
                        </a>
                    </span>
                </div>
                <div class="meta-item">
                    <strong>Fraktion:</strong>
                    <span class="badge">${redner.fraktion.name}</span>
                </div>
                <div class="meta-item">
                    <strong>Datum & Uhrzeit:</strong>
                    <span>${rede.start}</span>
                </div>
                <div class="meta-item">
                    <strong>Textlänge:</strong>
                    <span>${rede.textLaenge} Zeichen</span>
                </div>
                <div class="meta-item">
                    <strong>Kommentare:</strong>
                    <span class="count">${rede.kommentarAnzahl}</span>
                </div>
            </div>
        </div>
    </section>

    <!-- Redetext -->
    <section class="rede-text-section">
        <h2>💬 Wortlaut der Rede</h2>
        <div class="rede-text-card">
            <div class="rede-text-content">
                <#assign paragraphs = rede.text?split("\n")>
                <#list paragraphs as paragraph>
                    <#if paragraph?trim?length gt 0>
                        <p>${paragraph}</p>
                    </#if>
                </#list>
            </div>
        </div>
    </section>

    <!-- Kommentare -->
    <#if rede.kommentare?size gt 0>
        <section class="kommentare-section">
            <h2>💭 Kommentare (${rede.kommentare?size})</h2>
            <div class="kommentare-list">
                <#list rede.kommentare as kommentar>
                    <div class="kommentar-card">
                        <p>${kommentar.inhalt}</p>
                    </div>
                </#list>
            </div>
        </section>
    </#if>

    <!-- Aktionsbereich -->
    <section class="actions-section">
        <div class="actions-card">
            <a href="/abgeordneter/${redner.id}" class="btn btn-secondary">
                ← Alle Reden von ${redner.nachname}
            </a>
            <a href="/" class="btn btn-secondary">
                🏠 Zur Startseite
            </a>
        </div>
    </section>
</main>

<footer>
    <div class="container">
        <p>&copy; 2025 Bundestag Reden-Portal | Goethe Universität Frankfurt</p>
    </div>
</footer>
</body>
</html>