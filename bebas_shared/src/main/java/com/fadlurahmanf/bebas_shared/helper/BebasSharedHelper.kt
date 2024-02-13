package com.fadlurahmanf.bebas_shared.helper

import com.fadlurahmanf.bebas_shared.BebasShared

object BebasSharedHelper {
    fun getFullNameMonth(month: Int): String {
        when (BebasShared.language) {
            "en-US" -> {
                when (month) {
                    1 -> {
                        return "January"
                    }

                    2 -> {
                        return "February"
                    }

                    3 -> {
                        return "March"
                    }

                    4 -> {
                        return "April"
                    }

                    5 -> {
                        return "May"
                    }

                    6 -> {
                        return "June"
                    }

                    7 -> {
                        return "July"
                    }

                    8 -> {
                        return "August"
                    }

                    9 -> {
                        return "September"
                    }

                    10 -> {
                        return "October"
                    }

                    11 -> {
                        return "November"
                    }

                    12 -> {
                        return "December"
                    }

                    else -> {
                        return "-"
                    }
                }
            }

            else -> {
                when (month) {
                    1 -> {
                        return "Januari"
                    }

                    2 -> {
                        return "Februari"
                    }

                    3 -> {
                        return "Maret"
                    }

                    4 -> {
                        return "April"
                    }

                    5 -> {
                        return "Mei"
                    }

                    6 -> {
                        return "Juni"
                    }

                    7 -> {
                        return "Juli"
                    }

                    8 -> {
                        return "Augustus"
                    }

                    9 -> {
                        return "September"
                    }

                    10 -> {
                        return "Oktober"
                    }

                    11 -> {
                        return "November"
                    }

                    12 -> {
                        return "Desember"
                    }

                    else -> {
                        return "-"
                    }
                }
            }
        }
    }
}