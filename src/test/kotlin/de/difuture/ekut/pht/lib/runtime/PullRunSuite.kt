package de.difuture.ekut.pht.lib.runtime

import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * This Suite simply ensures that the pull tests run before the run tests (if we can't pull the test images,
 * we do not need to continue).
 *
 * This does not mean that we do not pull in other classes.
 *
 * @
 *
 */
@Suite.SuiteClasses(SpotifyDockerClientPullTests::class, SpotifyDockerClientRunTests::class)
@RunWith(Suite::class)
class PullRunSuite
