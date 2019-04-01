package no.ab.application2.fragments

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

abstract class FragmentHandler : Fragment() {

    private lateinit var context: FragmentActivity


    /**
     * Loads a new fragment and adding it to the backstack.
     */
    public fun pushFragmentWithStack(activity: Activity, container: Int, fragment: Fragment){
        context = activity as FragmentActivity

        context.supportFragmentManager
            .beginTransaction()
            .replace(container, fragment, null)
            .addToBackStack(null)
            .commit()

    }

    /**
     * Loads a new fragment without adding it to the backstack
     */
    public fun pushFragmentWithoutStack(activity: Activity, container: Int, fragment: Fragment){
        context = activity as FragmentActivity

        context.supportFragmentManager
            .beginTransaction()
            .replace(container, fragment, null)
            .commit()
    }

    /**
     * Pops a fragment the numbersOfPop entered from the stack
     * If no more fragments on stack -> exit
     *
     * @param activity the activity
     * @param numberOfPop the number of pops you want to do
     */
    public fun popFragment(activity: Activity, numberOfPop: Int){

        context = activity as FragmentActivity

        context.supportFragmentManager
            .popBackStack(context.supportFragmentManager
                .getBackStackEntryAt(context.supportFragmentManager
                    .backStackEntryCount-numberOfPop).id, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    }


    /**
     * Reloads a fragment
     */
    public fun reloadFragment(activity: Activity){
        context = activity as FragmentActivity

        context.supportFragmentManager
            .beginTransaction()
            .detach(this)
            .attach(this)
            .commit()
    }
}