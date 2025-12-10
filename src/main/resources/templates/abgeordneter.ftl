<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${abgeordneter.vorname} ${abgeordneter.nachname} - Bundestag Portal</title>
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

        /* Profil-Bereich */
        .profil-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 16px;
            overflow: hidden;
            box-shadow: 0 12px 40px rgba(102, 126, 234, 0.3);
        }

        .profil-header {
            padding: 2.5rem;
            color: white;
            text-align: center;
            background: rgba(0,0,0,0.1);
        }

        .profil-header h2 {
            color: white;
            font-size: 2.2rem;
            margin: 0;
        }

        .profil-body {
            background: white;
            padding: 2.5rem;
        }

        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 2rem;
        }

        .info-item {
            display: flex;
            flex-direction: column;
            gap: 0.5rem;
        }

        .info-item strong {
            color: #7f8c8d;
            font-size: 0.9rem;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .info-item span {
            font-size: 1.2rem;
            color: #2c3e50;
        }

        .badge {
            display: inline-block;
            padding: 0.5rem 1rem;
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
            border-radius: 20px;
            font-size: 0.95rem;
            font-weight: 600;
            box-shadow: 0 2px 8px rgba(240, 147, 251, 0.3);
        }

        .count {
            font-weight: 700;
            color: #667eea;
            font-size: 1.3rem;
        }

        /* Reden-Liste */
        .reden-list {
            display: grid;
            gap: 1.5rem;
        }

        .rede-card {
            background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
            border-radius: 16px;
            overflow: hidden;
            border: 2px solid #e0e6ed;
            transition: all 0.3s ease;
        }

        .rede-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 12px 40px rgba(0,0,0,0.12);
            border-color: #667eea;
        }

        .rede-header {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            padding: 1.5rem;
            color: white;
        }

        .rede-header h3 {
            margin: 0;
            font-size: 1.2rem;
            color: white;
        }

        .rede-header h3 a {
            color: white;
            text-decoration: none;
        }

        .rede-body {
            padding: 1.5rem;
        }

        .rede-stats {
            display: flex;
            gap: 2rem;
            margin-bottom: 1rem;
            padding-bottom: 1rem;
            border-bottom: 2px solid #e0e6ed;
        }

        .rede-stats .stat {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            font-size: 0.9rem;
        }

        .rede-preview {
            color: #5a6c7d;
            line-height: 1.7;
            font-size: 1rem;
        }

        .rede-footer {
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
                flex-wrap: wrap;
                justify-content: center;
            }

            .info-grid {
                grid-template-columns: 1fr;
            }

            section {
                padding: 1.5rem;
            }

            .rede-stats {
                flex-direction: column;
                gap: 0.5rem;
            }
        }

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

        .rede-card {
            animation: fadeIn 0.5s ease forwards;
            opacity: 0;
        }

        .rede-card:nth-child(1) { animation-delay: 0.1s; }
        .rede-card:nth-child(2) { animation-delay: 0.2s; }
        .rede-card:nth-child(3) { animation-delay: 0.3s; }
        .rede-card:nth-child(4) { animation-delay: 0.4s; }
    </style>
</head>
<body>
<header>
    <div class="container">
        <div class="header-top">
            <img src="/static/img/deutscher-bundestag-seeklogo.png" alt="Deutscher Bundestag" class="bundestag-logo">
            <nav>
                <a href="/">← Zurück zur Übersicht</a>
                <a href="/statistiken">Statistiken</a>
            </nav>
        </div>
    </div>
</header>

<main class="container">
    <!-- Profil-Bereich -->
    <section class="profil-section">
        <div class="profil-card">
            <div class="profil-header">
                <h2>👤 ${abgeordneter.vorname} ${abgeordneter.nachname}</h2>
            </div>
            <div class="profil-body">
                <div class="info-grid">
                    <div class="info-item">
                        <strong>ID:</strong>
                        <span>${abgeordneter.id}</span>
                    </div>
                    <div class="info-item">
                        <strong>Fraktion:</strong>
                        <span class="badge">${abgeordneter.fraktion.name}</span>
                    </div>
                    <div class="info-item">
                        <strong>Anzahl Reden:</strong>
                        <span class="count">${reden?size}</span>
                    </div>
                    <div class="info-item">
                        <strong>Ø Redelänge:</strong>
                        <span>${durchschnittRedelaenge?round} Zeichen</span>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Reden-Liste -->
    <section class="reden-section">
        <h2>📝 Alle Reden (${reden?size})</h2>

        <#if reden?size == 0>
            <p class="no-results">Keine Reden vorhanden.</p>
        <#else>
            <div class="reden-list">
                <#list reden as rede>
                    <div class="rede-card">
                        <div class="rede-header">
                            <h3>
                                <a href="/rede/${rede.id}">
                                    Rede vom ${rede.start}
                                </a>
                            </h3>
                        </div>
                        <div class="rede-body">
                            <div class="rede-stats">
                                <span class="stat">
                                    <strong>Länge:</strong>
                                    ${rede.textLaenge} Zeichen
                                </span>
                                <span class="stat">
                                    <strong>Kommentare:</strong>
                                    ${rede.kommentarAnzahl}
                                </span>
                            </div>
                            <p class="rede-preview">
                                ${(rede.text?length > 200)?then(rede.text?substring(0, 200) + "...", rede.text)}
                            </p>
                        </div>
                        <div class="rede-footer">
                            <a href="/rede/${rede.id}" class="btn">Vollständige Rede lesen →</a>
                        </div>
                    </div>
                </#list>
            </div>
        </#if>
    </section>
</main>

<footer>
    <div class="container">
        <p>&copy; 2025 Bundestag Reden-Portal | Goethe Universität Frankfurt</p>
    </div>
</footer>
</body>
</html>