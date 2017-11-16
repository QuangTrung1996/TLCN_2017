package docongphuc.pttravle.Maps

interface DirectionFinderListener {
    fun onDirectionFinderStart()
    fun onDirectionFinderSuccess(route: List<Route>)
}