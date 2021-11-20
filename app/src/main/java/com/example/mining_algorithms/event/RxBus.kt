package com.example.mining_algorithms.event

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object RxBus {
    private val publisher = PublishSubject.create<Any>()

    fun publish(event: Any) {
        publisher.onNext(event)
    }

    fun <T> subscribe(eventType: Class<T>): Observable<T> {
        return publisher.ofType(eventType)
    }
}