(function () {
    "use strict";

    const states = new Map();
    const HANDICAP_X_MAP = {
        0: 23.6,
        10: 20.2,
        20: 16.8
    };

    function clamp(value, min, max) {
        return Math.min(Math.max(value, min), max);
    }

    function setTokenPosition(token, xPercent, yPercent) {
        token.dataset.x = xPercent.toFixed(2);
        token.dataset.y = yPercent.toFixed(2);
        token.style.left = xPercent + "%";
        token.style.top = yPercent + "%";
    }

    function resolveDefaultPosition(token) {
        const handicap = Number(token.dataset.defaultHandicap);
        const mappedX = HANDICAP_X_MAP[handicap];
        const fallbackX = Number(token.dataset.defaultX);
        const defaultX = Number.isFinite(mappedX) ? mappedX : fallbackX;
        const defaultY = Number(token.dataset.defaultY);

        return {
            x: Number.isFinite(defaultX) ? defaultX : HANDICAP_X_MAP[0],
            y: Number.isFinite(defaultY) ? defaultY : 61
        };
    }

    function reset(boardId) {
        const state = states.get(boardId);
        if (!state) {
            return;
        }

        state.tokens.forEach(function (token) {
            const position = resolveDefaultPosition(token);
            setTokenPosition(token, position.x, position.y);
            token.classList.remove("is-dragging");
        });
        state.activeToken = null;
        state.activePointerId = null;
    }

    function setEnabled(boardId, enabled) {
        const state = states.get(boardId);
        if (!state) {
            return;
        }

        state.enabled = enabled;
        state.tokens.forEach(function (token) {
            token.style.pointerEvents = enabled ? "auto" : "none";
            token.classList.remove("is-dragging");
        });
    }

    function moveActiveToken(state, clientX, clientY) {
        if (!state.activeToken) {
            return;
        }

        const rect = state.board.getBoundingClientRect();
        const tokenHalfWidth = state.activeToken.offsetWidth / 2;
        const tokenHalfHeight = state.activeToken.offsetHeight / 2;
        const rawX = clientX - rect.left;
        const rawY = clientY - rect.top;
        const clampedX = clamp(rawX, tokenHalfWidth, rect.width - tokenHalfWidth);
        const clampedY = clamp(rawY, tokenHalfHeight, rect.height - tokenHalfHeight);
        const xPercent = (clampedX / rect.width) * 100;
        const yPercent = (clampedY / rect.height) * 100;

        setTokenPosition(state.activeToken, xPercent, yPercent);
    }

    function finishDrag(state, event) {
        const token = state.activeToken;
        if (!token || state.activePointerId !== event.pointerId) {
            return;
        }

        token.classList.remove("is-dragging");
        if (typeof token.releasePointerCapture === "function"
            && typeof token.hasPointerCapture === "function"
            && token.hasPointerCapture(event.pointerId)) {
            try {
                token.releasePointerCapture(event.pointerId);
            } catch (error) {
                // Pointer capture can already be released by the browser.
            }
        }
        state.activeToken = null;
        state.activePointerId = null;
    }

    function bindEvents(state) {
        state.tokens.forEach(function (token) {
            token.addEventListener("pointerdown", function (event) {
                if (state.enabled === false || token.style.pointerEvents === "none") {
                    return;
                }
                event.preventDefault();
                state.activeToken = token;
                state.activePointerId = event.pointerId;
                token.classList.add("is-dragging");
                if (typeof token.setPointerCapture === "function") {
                    try {
                        token.setPointerCapture(event.pointerId);
                    } catch (error) {
                        // Document-level pointermove keeps dragging usable without capture.
                    }
                }
                moveActiveToken(state, event.clientX, event.clientY);
            });
        });

        document.addEventListener("pointermove", function (event) {
            if (!state.activeToken || state.activePointerId !== event.pointerId) {
                return;
            }
            event.preventDefault();
            moveActiveToken(state, event.clientX, event.clientY);
        }, { passive: false });

        document.addEventListener("pointerup", function (event) {
            finishDrag(state, event);
        });

        document.addEventListener("pointercancel", function (event) {
            finishDrag(state, event);
        });

        if (state.resetButton) {
            state.resetButton.addEventListener("click", function () {
                reset(state.boardId);
            });
        }
    }

    function init(options) {
        const boardId = options && options.boardId ? options.boardId : "startDiagramBoard";
        const resetButtonId = options && options.resetButtonId ? options.resetButtonId : "resetStartDiagram";

        if (states.has(boardId)) {
            return states.get(boardId);
        }

        const board = document.getElementById(boardId);
        if (!board) {
            return null;
        }

        const state = {
            boardId: boardId,
            board: board,
            resetButton: document.getElementById(resetButtonId),
            tokens: Array.from(board.querySelectorAll(".start-token")),
            enabled: true,
            activeToken: null,
            activePointerId: null
        };

        bindEvents(state);
        states.set(boardId, state);
        reset(boardId);
        return state;
    }

    window.AutoRaceStartDiagram = {
        init: init,
        reset: reset,
        setEnabled: setEnabled
    };
})();
