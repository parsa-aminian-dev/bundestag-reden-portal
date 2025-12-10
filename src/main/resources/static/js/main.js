/**
 * Haupt-JavaScript für dynamische Funktionalität.
 * Implementiert Live-Suche mit jQuery gemäß Aufgabenstellung.
 *
 * @author PARSA AMINIAN
 * @version 1.0
 */

$(document).ready(function() {

    // ==================== LIVE-SUCHE ====================

    let searchTimeout;

    $('#searchInput').on('input', function() {
        const query = $(this).val().trim();

        // Debouncing: Warte 300ms nach letzter Eingabe
        clearTimeout(searchTimeout);

        if (query.length < 2) {
            $('#searchResults').empty().hide();
            return;
        }

        searchTimeout = setTimeout(function() {
            performSearch(query);
        }, 300);
    });

    /**
     * Führt die Suche via REST-API durch.
     *
     * @param {string} query Suchbegriff
     */
    function performSearch(query) {
        $.ajax({
            url: '/api/abgeordnete/search',
            method: 'GET',
            data: { q: query },
            dataType: 'json',
            success: function(data) {
                displaySearchResults(data);
            },
            error: function(xhr, status, error) {
                console.error('Suchfehler:', error);
                $('#searchResults').html(
                    '<div style="padding: 15px; color: red;">Fehler bei der Suche</div>'
                ).show();
            }
        });
    }

    /**
     * Zeigt Suchergebnisse an.
     *
     * @param {Array} results Array von Abgeordneten-Objekten
     */
    function displaySearchResults(results) {
        const $resultsDiv = $('#searchResults');

        if (results.length === 0) {
            $resultsDiv.html(
                '<div style="padding: 15px; color: #666;">Keine Ergebnisse gefunden</div>'
            ).show();
            return;
        }

        let html = '<ul>';

        results.forEach(function(abg) {
            html += `
                <li>
                    <a href="/abgeordneter/${abg.id}">
                        <strong>${abg.vorname} ${abg.nachname}</strong><br>
                        <small>
                            <span class="badge" style="font-size: 0.8rem;">${abg.fraktion}</span>
                            | ${abg.redenAnzahl} Reden
                        </small>
                    </a>
                </li>
            `;
        });

        html += '</ul>';

        $resultsDiv.html(html).show();
    }

    // Schließe Suchergebnisse bei Klick außerhalb
    $(document).on('click', function(e) {
        if (!$(e.target).closest('.search-box').length) {
            $('#searchResults').hide();
        }
    });

    // ==================== STATISTIKEN DYNAMISCH LADEN ====================

    if ($('.stats-section').length > 0) {
        loadDynamicStatistics();
    }

    /**
     * Lädt Statistiken dynamisch via AJAX.
     */
    function loadDynamicStatistics() {
        $.ajax({
            url: '/api/statistiken',
            method: 'GET',
            dataType: 'json',
            success: function(data) {
                console.log('Statistiken geladen:', data);

            },
            error: function(xhr, status, error) {
                console.error('Fehler beim Laden der Statistiken:', error);
            }
        });
    }

    // ==================== SMOOTH SCROLL ====================

    $('a[href^="#"]').on('click', function(e) {
        e.preventDefault();

        const target = $(this.getAttribute('href'));

        if (target.length) {
            $('html, body').stop().animate({
                scrollTop: target.offset().top - 100
            }, 800);
        }
    });

    // ==================== TOOLTIPS ====================

    $('[data-tooltip]').hover(
        function() {
            const tooltip = $(this).data('tooltip');
            $('<div class="tooltip">' + tooltip + '</div>')
                .appendTo('body')
                .fadeIn(200);
        },
        function() {
            $('.tooltip').remove();
        }
    );

    // ==================== CARD ANIMATIONS ====================

    $('.abgeordneter-card, .rede-card, .stat-card').each(function(index) {
        $(this).css('animation-delay', (index * 0.1) + 's');
    });

    console.log('Reden-Portal JavaScript initialisiert ✓');
});