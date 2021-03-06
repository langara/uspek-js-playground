package playground

import uspek.eq
import uspek.o
import uspek.ox
import uspek.uspek

class MicroCalc(var result: Int) {
    fun add(x: Int) { result += x }
    fun multiplyBy(x: Int) { result *= x }
}

suspend fun example() = uspek {
    "create SUT" o {

        val sut = MicroCalc(10)

        "check add" o {
            sut.add(5)
            sut.result eq 15
            sut.add(100)
            sut.result eq 115
        }

        "mutate SUT" o {
            sut.add(1)

            "incorrectly check add - this should fail" o {
                sut.add(5)
                sut.result eq 15
            }
        }

        "check add again" o {
            sut.add(5)
            sut.result eq 15
            sut.add(100)
            sut.result eq 115
        }

        testSomeAdding(sut)

        "mutate SUT and check multiplyBy" o {
            sut.result = 3

            sut.multiplyBy(3)
            sut.result eq 9
            sut.multiplyBy(4)
            sut.result eq 36

            testSomeAdding(sut)

            1 eq 2
        }

        "assure that SUT is intact by any of sub tests above" o {
            sut.result eq 10
        }
    }

}

private suspend fun testSomeAdding(calc: MicroCalc) {
    val start = calc.result
    "add 5 to $start" o {
        calc.add(5)
        val afterAdd5 = start + 5
        "result should be $afterAdd5" o { calc.result eq afterAdd5 }

        "add 7 more" o {
            calc.add(7)
            val afterAdd5Add7 = afterAdd5 + 7
            "result should be $afterAdd5Add7" o { calc.result eq afterAdd5Add7 }
        }
    }

    "subtract 3" o {
        calc.add(-3)
        val afterSub3 = start - 3
        "result should be $afterSub3" o { calc.result eq afterSub3 }
    }

}
