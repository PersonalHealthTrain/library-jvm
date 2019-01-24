package de.difuture.ekut.pht.lib.train.station

import de.difuture.ekut.pht.lib.train.api.RebaseStrategy
import de.difuture.ekut.pht.lib.train.api.RunResponse
import de.difuture.ekut.pht.lib.train.api.TrainOutput

sealed class TrainException(msg: String?) : Exception(msg) {

    /**
     * Exception to be thrown when the TrainOutput contains an error message
     */
    data class Output(val output: TrainOutput<*>) : TrainException(output.error)

    /**
     * Exception to be thrown when the 'success' field of the RunAlgorithmResponse failed
     */
    data class RunAlgorithmFailed(val output: TrainOutput<RunResponse>)
        : TrainException(output.response?.free_text_message)

    data class UnsupportedRebaseStrategy(val rebase: RebaseStrategy)
        : TrainException("Unsupported Rebase Strategy: {}. Train cannot depart.".format(rebase.display))
}
