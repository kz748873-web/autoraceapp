(function () {
    "use strict";

    const storageKey = "selectedRacePlace";
    const legacyVenueStorageKey = "autorace:selectedVenue";
    const venueNames = {
        kawaguchi: "川口",
        isesaki: "伊勢崎",
        hamamatsu: "浜松",
        iizuka: "飯塚",
        sanyo: "山陽"
    };
    const placeByVenueName = {
        "川口": "kawaguchi",
        "伊勢崎": "isesaki",
        "浜松": "hamamatsu",
        "飯塚": "iizuka",
        "山陽": "sanyo"
    };
    const placeKeys = Object.keys(venueNames);
    const themeClasses = placeKeys.map(function (placeKey) {
        return "theme-" + placeKey;
    });

    function safeStorageGet(key) {
        try {
            return window.localStorage.getItem(key);
        } catch (error) {
            return "";
        }
    }

    function safeStorageSet(key, value) {
        try {
            window.localStorage.setItem(key, value);
        } catch (error) {
            return;
        }
    }

    function normalizePlace(value) {
        const rawValue = value ? String(value).trim() : "";
        if (!rawValue) {
            return "";
        }

        const normalizedValue = rawValue.replace(/^theme-/, "").toLowerCase();
        if (venueNames[normalizedValue]) {
            return normalizedValue;
        }

        return placeByVenueName[rawValue] || "";
    }

    function getPlaceFromBody() {
        const body = document.body;
        if (!body) {
            return "";
        }

        const datasetPlace = body.dataset ? normalizePlace(body.dataset.selectedRacePlace) : "";
        if (datasetPlace) {
            return datasetPlace;
        }

        const datasetVenue = body.dataset ? normalizePlace(body.dataset.selectedVenue) : "";
        if (datasetVenue) {
            return datasetVenue;
        }

        return placeKeys.find(function (placeKey) {
            return body.classList.contains("theme-" + placeKey);
        }) || "";
    }

    function getStoredPlace() {
        const storedPlace = normalizePlace(safeStorageGet(storageKey));
        if (storedPlace) {
            return storedPlace;
        }

        return normalizePlace(safeStorageGet(legacyVenueStorageKey));
    }

    function writeStoredPlace(placeKey) {
        const normalizedPlace = normalizePlace(placeKey);
        if (!normalizedPlace) {
            return;
        }

        safeStorageSet(storageKey, normalizedPlace);
        safeStorageSet(legacyVenueStorageKey, venueNames[normalizedPlace]);
    }

    function applyPlace(placeKey, options) {
        const body = document.body;
        const normalizedPlace = normalizePlace(placeKey);
        const shouldPersist = !options || options.persist !== false;

        if (!body || !normalizedPlace) {
            return "";
        }

        themeClasses.forEach(function (themeClass) {
            body.classList.remove(themeClass);
        });
        body.classList.add("theme-" + normalizedPlace);

        if (body.dataset) {
            body.dataset.selectedRacePlace = normalizedPlace;
            body.dataset.selectedVenue = venueNames[normalizedPlace];
        }

        if (shouldPersist) {
            writeStoredPlace(normalizedPlace);
        }

        try {
            window.dispatchEvent(new CustomEvent("predictionpit:venue-theme-change", {
                detail: {
                    place: normalizedPlace,
                    venueName: venueNames[normalizedPlace]
                }
            }));
        } catch (error) {
            return normalizedPlace;
        }

        return normalizedPlace;
    }

    function applyFromVenueName(venueName, options) {
        return applyPlace(normalizePlace(venueName), options);
    }

    function getVenueName(placeKey) {
        return venueNames[normalizePlace(placeKey)] || "";
    }

    function getCurrentPlace() {
        return getPlaceFromBody() || getStoredPlace();
    }

    function getCurrentVenueName() {
        return getVenueName(getCurrentPlace());
    }

    function initializeVenueTheme() {
        const bodyPlace = getPlaceFromBody();
        const storedPlace = getStoredPlace();
        const initialPlace = bodyPlace || storedPlace;

        if (initialPlace) {
            applyPlace(initialPlace, { persist: true });
        }
    }

    document.addEventListener("DOMContentLoaded", function () {
        try {
            initializeVenueTheme();
        } catch (error) {
            if (window.console && typeof window.console.warn === "function") {
                window.console.warn("Venue theme could not start.", error);
            }
        }
    });

    document.addEventListener("change", function (event) {
        const target = event.target;
        if (!target || !target.matches || !target.matches("[data-race-place-select], select[name='venue'], select#venue")) {
            return;
        }

        applyFromVenueName(target.value, { persist: true });
    });

    document.addEventListener("click", function (event) {
        const target = event.target && event.target.closest ? event.target.closest("[data-theme-key], [data-race-place]") : null;
        if (!target || !target.dataset) {
            return;
        }

        applyPlace(target.dataset.themeKey || target.dataset.racePlace, { persist: true });
    });

    window.PredictionPitVenueTheme = {
        storageKey: storageKey,
        placeKeys: placeKeys.slice(),
        venueNames: Object.assign({}, venueNames),
        themeClasses: themeClasses.slice(),
        normalizePlace: normalizePlace,
        getStoredPlace: getStoredPlace,
        getCurrentPlace: getCurrentPlace,
        getCurrentVenueName: getCurrentVenueName,
        getVenueName: getVenueName,
        getPlaceFromVenueName: function (venueName) {
            return normalizePlace(venueName);
        },
        setPlace: applyPlace,
        applyFromVenueName: applyFromVenueName,
        persistPlace: writeStoredPlace
    };
})();
