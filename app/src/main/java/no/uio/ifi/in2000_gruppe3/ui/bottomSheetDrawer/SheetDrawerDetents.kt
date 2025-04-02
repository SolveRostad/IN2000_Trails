package no.uio.ifi.in2000_gruppe3.ui.bottomSheetDrawer

import com.composables.core.SheetDetent

enum class SheetDrawerDetent(val value: SheetDetent) {
    HIDDEN(SheetDetent(identifier = "hidden") { containerHeight, sheetHeight ->
        containerHeight * 0.04f
    }),
    SEMIPEEK(SheetDetent(identifier = "semiPeek") { containerHeight, sheetHeight ->
        containerHeight * 0.15f
    }),
    PEEK(SheetDetent(identifier = "peek") { containerHeight, sheetHeight ->
        containerHeight * 0.5f
    }),
    FULLYEXPAND(SheetDetent(identifier = "fullyExpanded") { containerHeight, sheetHeight ->
        containerHeight * 1.0f
    })
}
