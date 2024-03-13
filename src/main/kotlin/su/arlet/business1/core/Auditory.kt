package su.arlet.business1.core

import su.arlet.business1.core.rates.AgeRate
import su.arlet.business1.core.rates.HobbyRate
import su.arlet.business1.core.rates.IncomeRate
import su.arlet.business1.core.rates.LocationRate

class Auditory(
    val ageRate: AgeRate,
    val incomeRate: IncomeRate,
    val hobbyRate: HobbyRate,
    val locationRate: LocationRate,
)