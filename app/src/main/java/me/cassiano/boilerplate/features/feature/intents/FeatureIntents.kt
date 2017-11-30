package me.cassiano.boilerplate.features.feature.intents

sealed class FeatureIntents {
    class InitialLoadIntent : FeatureIntents()
    class PullToRefreshIntent : FeatureIntents()
}