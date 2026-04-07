(function () {
    "use strict";

    const states = new Map();

    function clamp(value, min, max) {
        return Math.min(Math.max(value, min), max);
    }

    function setTokenPosition(token, xPercent, yPercent) {
        token.dataset.x = xPercent.toFixed(2);
        token.dataset.y = yPercent.toFixed(2);
        token.style.left = xPercent + "%";
        token.style.top = yPercent + "%";
    }

    function reset(boardId) {
        const state = states.get(boardId);
        if (!state) {
            return;
        }

        state.tokens.forEach(function (token) {
            const defaultX = Number(token.dataset.defaultX);
            const defaultY = Number(token.dataset.defaultY);
            setTokenPosition(token, defaultX, defaultY);
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

    function bindEvents(state) {
        state.tokens.forEach(function (token) {
            token.addEventListener("pointerdown", function (event) {
                if (token.style.pointerEvents === "none") {
                    return;
                }
                state.activeToken = token;
                state.activePointerId = event.pointerId;
                token.classList.add("is-dragging");
                token.setPointerCapture(event.pointerId);
                moveActiveToken(state, event.clientX, event.clientY);
            });

            token.addEventListener("pointermove", function (event) {
                if (state.activeToken !== token || state.activePointerId !== event.pointerId) {
                    return;
                }
                moveActiveToken(state, event.clientX, event.clientY);
            });

            function finishDrag(event) {
                if (state.activeToken !== token || state.activePointerId !== event.pointerId) {
                    return;
                }
                token.classList.remove("is-dragging");
                state.activeToken = null;
                state.activePointerId = null;
            }

            token.addEventListener("pointerup", finishDrag);
            token.addEventListener("pointercancel", finishDrag);
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
