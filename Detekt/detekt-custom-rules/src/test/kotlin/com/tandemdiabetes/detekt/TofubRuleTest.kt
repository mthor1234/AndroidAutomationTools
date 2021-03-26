/**
 * Copyright Tandem Diabetes Care, Inc. 2020.  All rights reserved.
 * TofubRuleTest.kt
 * Tests the TofubRule.kt
 * @author: Mitch Thornton Nov 04, 2020
 */

package com.tandemdiabetes.detekt

import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.lint
import org.junit.Test

class TofubRuleTest {

    @Test
    fun `buildExpectedTOFUBRegex() Description Expect No Issues`() {
        val descriptionFunction = " /**\n" +
            "      * This only validates the site change.\n" +
            "      */\n" +
            "    fun isSiteChangeSet() {\n" +
            "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(0)
    }

    @Test
    fun `buildExpectedTOFUBRegex() Multi Line Description Expect No Issues`() {
        val descriptionFunction = "/**\n" +
            "     * This only validates the site change.\n" +
            "     * This only validates the site change.\n" +
            "     * This only validates the site change.\n" +
            "     */\n" +
            "    fun isSiteChangeSet() {\n" +
            "        return siteChangeThreshold != 0\n" +
            "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(0)
    }

    @Test
    fun `buildExpectedTOFUBRegex() Description Param Expect No Issues`() {
        val descriptionFunction = "/**\n" +
            "     * This only validates the site change.\n" +
            "     * @param test this is a test param\n" +
            "     */\n" +
            "    fun isSiteChangeSet(test : Boolean) {\n" +
            "        return siteChangeThreshold != 0\n" +
            "    } // isSiteChangeSet"

        assertThat(TofubRule().lint(descriptionFunction)).hasSize(0)
    }

    @Test
    fun `buildExpectedTOFUBRegex() Description Param Param Expect No Issues`() {
        val descriptionFunction = "/**\n" +
            "     * This only validates the site change.\n" +
            "     * @param test this is a test param\n" +
            "     * @param testTwo this is a test param\n" +
            "     */\n" +
            "    fun isSiteChangeSet(test : Boolean, testTwo : Boolean) {\n" +
            "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(0)
    }

    @Test
    fun `buildExpectedTOFUBRegex() Description Param Return Expect No Issues`() {
        val descriptionFunction = "/**\n" +
            "     * This only validates the site change.\n" +
            "     * @param test this is a test param\n" +
            "     * @return Boolean - returns true if and only if all of the site change is valid.\n" +
            "     */\n" +
            "    fun isSiteChangeSet(test : Boolean): Boolean {\n" +
            "        return siteChangeThreshold != 0\n" +
            "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(0)
    }

    @Test
    fun `buildExpectedTOFUBRegex() Description Param Return Throws Expect No Issues`() {
        val descriptionFunction = "/**\n" +
            "     * This only validates the site change.\n" +
            "     * @param test this is a test param\n" +
            "     * @return Boolean - returns true if and only if all of the site change is valid.\n" +
            "     * @throws test this is a throw\n" +
            "     */\n" +
            "    fun isSiteChangeSet(test : Boolean): Boolean {\n" +
            "       throw IllegalArgumentException(\"Test Test\")\n" +
            "        return siteChangeThreshold != 0\n" +
            "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(0)
    }

    @Test
    fun `buildExpectedTOFUBRegex() Description Param Return Throws Catch Expect No Issues`() {
        val descriptionFunction = "/**\n" +
            "     * This only validates the site change.\n" +
            "     * @param test this is a test param\n" +
            "     * @return Boolean - returns true if and only if all of the site change is valid.\n" +
            "     * @throws This is a test throw\n" +
            "     * @catch This is a test catch\n" +
            "     */\n" +
            "    fun isSiteChangeSet(test : Boolean): Boolean {\n" +
            "       throw IllegalArgumentException(\"Test Throw\")\n" +
            "       try{" +
            "       catch(IllegalArgumentException(\"Test Throw\")\n" +
            "       }\n" +
            "        return siteChangeThreshold != 0\n" +
            "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(0)
    }

    @Test
    fun `buildExpectedTOFUBRegex() Multiple Throws Expect No Issues`() {
        val descriptionFunction = "/**\n" +
            "     * This only validates the site change.\n" +
            "     * @param test this is a test param\n" +
            "     * @return Boolean - returns true if and only if all of the site change is valid.\n" +
            "     * @throws This is a test throw\n" +
            "     * @throws This is a test throw\n" +
            "     */\n" +
            "    fun isSiteChangeSet(test : Boolean): Boolean {\n" +
            "       throw IllegalArgumentException(\"Test Throw\")\n" +
            "       throw IllegalArgumentException(\"Test Throw\")\n" +
            "        return siteChangeThreshold != 0\n" +
            "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(0)
    }

    @Test
    fun `buildExpectedTOFUBRegex() Missing TOFUB Expect One Issue`() {
        val descriptionFunction =
            "    fun isSiteChangeSet(test : Boolean): Boolean {\n" +
                "        return siteChangeThreshold != 0\n" +
                "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(1)
    }

    @Test
    fun `buildExpectedTOFUBRegex() TOFUB Missing Description Expect One Issue`() {
        val descriptionFunction = "    /**\n" +
            "     * @param test this is a test param\n" +
            "     * @return Boolean - returns true if and only if all of the site change is valid.\n" +
            "     */\n" +
            "    fun isSiteChangeSet(test : Boolean): Boolean {\n" +
            "        return siteChangeThreshold != 0\n" +
            "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(1)
    }

    @Test
    fun `buildExpectedTOFUBRegex() TOFUB Missing Param Expect One Issue`() {
        val descriptionFunction = "    /**\n" +
            "     * This only validates the site change.\n" +
            "     * @return Boolean - returns true if and only if all of the site change is valid.\n" +
            "     */\n" +
            "    fun isSiteChangeSet(test : Boolean): Boolean {\n" +
            "        return siteChangeThreshold != 0\n" +
            "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(1)
    }

    @Test
    fun `buildExpectedTOFUBRegex() TOFUB Multiple Params, Missing One Param Expect One Issue`() {
        val descriptionFunction = "    /**\n" +
            "     * This only validates the site change.\n" +
            "     * @param test this is a test param\n" +
            "     * @return Boolean - returns true if and only if all of the site change is valid.\n" +
            "     */\n" +
            "    fun isSiteChangeSet(test : Boolean, testTwo : Boolean): Boolean {\n" +
            "        return siteChangeThreshold != 0\n" +
            "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(1)
    }

    @Test
    fun `buildExpectedTOFUBRegex() TOFUB Missing Return Expect One Issue`() {
        val descriptionFunction = "    /**\n" +
            "     * This only validates the site change.\n" +
            "     * @param test this is a test param\n" +
            "     */\n" +
            "    fun isSiteChangeSet(test : Boolean): Boolean {\n" +
            "        return siteChangeThreshold != 0\n" +
            "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(1)
    }

    @Test
    fun `buildExpectedTOFUBRegex() TOFUB Missing Throw Expect One Issue`() {
        val descriptionFunction = "    /**\n" +
            "     * This only validates the site change.\n" +
            "     * @param test this is a test param\n" +
            "     * @return Boolean - returns true if and only if all of the site change is valid.\n" +
            "     */\n" +
            "    fun isSiteChangeSet(test : Boolean): Boolean {\n" +
            "       throw IllegalArgumentException(\"Test Throw\")\n" +
            "        return siteChangeThreshold != 0\n" +
            "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(1)
    }

    @Test
    fun `buildExpectedTOFUBRegex() TOFUB Missing Throws Expect One Issue`() {
        val descriptionFunction = "    /**\n" +
            "     * This only validates the site change.\n" +
            "     * @param test this is a test param\n" +
            "     * @return Boolean - returns true if and only if all of the site change is valid.\n" +
            "     * @throws This is a test throw\n" +
            "     */\n" +
            "    fun isSiteChangeSet(test : Boolean): Boolean {\n" +
            "       throw IllegalArgumentException(\"Test Throw\")\n" +
            "       throw IllegalArgumentException(\"Test Throw\")\n" +
            "        return siteChangeThreshold != 0\n" +
            "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(1)
    }

    @Test
    fun `buildExpectedTOFUBRegex() TOFUB Missing Catch Expect One Issue`() {
        val descriptionFunction = "    /**\n" +
            "     * This only validates the site change.\n" +
            "     * @param test this is a test param\n" +
            "     * @return Boolean - returns true if and only if all of the site change is valid.\n" +
            "     */\n" +
            "    fun isSiteChangeSet(test : Boolean): Boolean {\n" +
            " try {\n" +
            "            view.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(\"market://details?id\")))\n" +
            "        } catch (anfe: android.content.ActivityNotFoundException) {\n" +
            "            view.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(\"https://play.google.com/store/apps/details?id=\")))\n" +
            "        }"
        "        return siteChangeThreshold != 0\n" +
            "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(1)
    }

    @Test
    fun `buildExpectedTOFUBRegex() TOFUB Extra Return Expect One Issue`() {
        val descriptionFunction = "    /**\n" +
            "     * This only validates the site change.\n" +
            "     * @param test this is a test param\n" +
            "     * @return Boolean - returns true if and only if all of the site change is valid.\n" +
            "     * @return Boolean - returns true if and only if all of the site change is valid.\n" +
            "     */\n" +
            "    fun isSiteChangeSet(test : Boolean): Boolean {\n" +
            "        return siteChangeThreshold != 0\n" +
            "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(1)
    }

    @Test
    fun `buildExpectedTOFUBRegex() TOFUB Extra Param Expect One Issue`() {
        val descriptionFunction = "    /**\n" +
            "     * This only validates the site change.\n" +
            "     * @param test this is a test param\n" +
            "     * @return Boolean - returns true if and only if all of the site change is valid.\n" +
            "     */\n" +
            "    fun isSiteChangeSet(): Boolean {\n" +
            "        return siteChangeThreshold != 0\n" +
            "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(1)
    }

    @Test
    fun `buildExpectedTOFUBRegex() Test`() {
        val descriptionFunction = "/**\n" +
            "     * This only validates the site change.\n" +
            "     * This only validates the site change.\n" +
            "     * This only validates the site change.\n" +
            "     * This only validates the site change.\n" +
            "     * This only validates the site change.\n" +
            "     *\n" +
            "     *\n" +
            "     * 1. This only validates the site change.\n" +
            "     * 2. This only validates the site change.\n" +
            "     * ........\n" +
            "     * @return ListenableFuture<Result> this is a test\n" +
            "     */\n" +
            "    fun isSiteChangeSet(): ListenableFuture<Result> {\n" +
            "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(0)
    }

    @Test
    fun `buildExpectedTOFUBRegex() TOFUB Multi Line Param Expect No Issues`() {
        val descriptionFunction = "/**\n" +
            "     * This only validates the site change.\n" +
            "     * @param test this is a test param.....\n" +
            "     * That continues onto the next line\n" +
            "     */\n" +
            "    fun isSiteChangeSet(test : Boolean) {\n" +
            "    } // isSiteChangeSet"
        assertThat(TofubRule().lint(descriptionFunction)).hasSize(0)
    }

    val kDOCTestString = "/**\n" +
        " * Copyright Tandem Diabetes Care 2018-2019.  All rights reserved.\n" +
        " * This class represents the Device Model of the phone it self.\n" +
        " * @author: John Sabillia\n" +
        " */\n" +
        "package com.tandemdiabetes.models\n" +
        "\n" +
        "import android.content.Context\n" +
        "import android.util.DisplayMetrics\n" +
        "import android.view.WindowManager\n" +
        "import androidx.lifecycle.MutableLiveData\n" +
        "import com.dexprotector.annotations.ClassEncryption\n" +
        "import com.tandemdiabetes.tconnect.BuildConfig\n" +
        "import com.tandemdiabetes.tconnect.logging.TLog\n" +
        "\n" +
        "@ClassEncryption\n" +
        "object DeviceModel {\n" +
        "\n" +
        "    private const val TAG = \"Dots\"\n" +
        "    var screenHeightPx = 0\n" +
        "    var screenWidthPx = 0\n" +
        "    var screenDensity = 0F\n" +
        "    var hasNetworkConnectivityLiveData = MutableLiveData<Boolean>()\n" +
        "\n" +
        "    /**\n" +
        "     * Function used to initialize pump dimension and pixel density\n" +
        "     * This is used on the graph so that we can adjust padding depending on the screen size.\n" +
        "     * @param context application context\n" +
        "     */\n" +
        "    @Synchronized\n" +
        "    fun init(context: Context) {\n" +
        "        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager\n" +
        "        val display = windowManager.defaultDisplay\n" +
        "        val metrics = DisplayMetrics()\n" +
        "        display.getMetrics(metrics)\n" +
        "\n" +
        "        screenHeightPx = metrics.heightPixels\n" +
        "        screenWidthPx = metrics.widthPixels\n" +
        "        screenDensity = metrics.density\n" +
        "    }\n" +
        "\n" +
        "    /**\n" +
        "     * Sets the state of the network connection to connected\n" +
        "     */\n" +
        "    @Synchronized\n" +
        "    fun setNetworkConnected() {\n" +
        "\n" +
        "        hasNetworkConnectivityLiveData.postValue(true)\n" +
        "        if (BuildConfig.DEBUG_LOG) {\n" +
        "            TLog.d(TAG, \"Network connected\")\n" +
        "        }\n" +
        "    }\n" +
        "\n" +
        "    /**\n" +
        "     * Sets the state of the network connection to disconnected\n" +
        "     */\n" +
        "    @Synchronized\n" +
        "    fun setNetworkDisconnected() {\n" +
        "\n" +
        "        hasNetworkConnectivityLiveData.postValue(false)\n" +
        "        if (BuildConfig.DEBUG_LOG) {\n" +
        "            TLog.d(TAG, \"Network disconnected\")\n" +
        "        }\n" +
        "    }\n" +
        "\n" +
        "    /**\n" +
        "     * Returns the value of network connection\n" +
        "     * @throws This is a test throw\n" +
        "     */\n" +
        "    @Synchronized\n" +
        "    fun getNetworkStatus(): Boolean? {\n" +
        "      return  hasNetworkConnectivityLiveData.value\n" +
        "    }\n" +
        "}\n" +
        "\n" +
        "\n" +
        "    /**\n" +
        "     * Overrides the onViewCreated add functionality to the view specific to this fragment.\n" +
        "     * This includes initializing the recycler view, populating it with any notifications,\n" +
        "     * and registering for changes in notifications\n" +
        "     * @param view: View that was created\n" +
        "     * @param savedInstanceState - provides bundled saved instance state\n" +
        "     * @return Boolean this is a test return\n" +
        "     * @throws This is a test throw\n" +
        "     * @exception This is a test exception\n" +
        "     */\n" +
        "    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {\n" +
        "        super.onViewCreated(view, savedInstanceState)\n" +
        "\n" +
        "        // If the fragment does not have a reference to the application,\n" +
        "        // then it would not have any notifications to display.\n" +
        "        // We expect this fragment to have a reference to the application,\n" +
        "        // because it is created in the DashboardActivity\n" +
        "        activity?.application?.let { it ->\n" +
        "\n" +
        "            (it as App).notifications.let { notifications ->\n" +
        "\n" +
        "                notifications.isOnNotificationsScreen = true\n" +
        "\n" +
        "                if (!PumpModel.getIsDisconnected()) {\n" +
        "                    notifications.markAllAsRead()\n" +
        "                    NotificationManagerCompat.from(it).cancelAll()\n" +
        "                }\n" +
        "\n" +
        "                viewManager = LinearLayoutManager(context)\n" +
        "                viewAdapter = NotificationsListAdapter(NotificationDiffCallback(), notifications)\n" +
        "\n" +
        "                notificationsRecyclerView.apply {\n" +
        "                    setHasFixedSize(true)\n" +
        "                    layoutManager = viewManager\n" +
        "                    adapter = viewAdapter\n" +
        "                }\n" +
        "\n" +
        "                val itemTouchHelper = ItemTouchHelper(SwipeLeftToDeleteCallback(notifications))\n" +
        "                itemTouchHelper.attachToRecyclerView(notificationsRecyclerView)\n" +
        "\n" +
        "                // Subscribe to notifications changes\n" +
        "                notifications.notificationsChangedCallback = {\n" +
        "\n" +
        "                    notificationsRecyclerView.adapter.let {\n" +
        "                        /*\n" +
        "                         * Update recyclerView first position to handle the empty view state\n" +
        "                         * The recyclerView will always have at least one element in it\n" +
        "                         * It will have either one viewHolder displaying text for the empty state\n" +
        "                         * or X number of viewHolders where X is the number of active notifications\n" +
        "                         */\n" +
        "                        (it as NotificationsListAdapter).notifyItemChanged(0)\n" +
        "                        // Update with new list\n" +
        "                        it.submitList(notifications.get())\n" +
        "                    }\n" +
        "                }\n" +
        "\n" +
        "                // Get initial notifications\n" +
        "                notificationsRecyclerView.adapter.let {\n" +
        "\n" +
        "                    (it as NotificationsListAdapter).submitList(notifications.get())\n" +
        "                }\n" +
        "            }\n" +
        "        } ?: run {\n" +
        "            if (BuildConfig.DEBUG_LOG) {\n" +
        "                TLog.e(TAG, \"Did not have a reference to the application to display notifications\")\n" +
        "            }\n" +
        "        }\n" +
        "\n" +
        "\n" +
        "        // Setup swipe refresh offset\n" +
        "        notifications_swipe_refresh_layout.setProgressViewOffset(true,\n" +
        "                resources.getDimensionPixelSize(R.dimen.SWIPE_REFRESH_OFFSET_START),\n" +
        "                resources.getDimensionPixelSize(R.dimen.SWIPE_REFRESH_OFFSET_END))\n" +
        "\n" +
        "        // Setup swipe refresh listener\n" +
        "        notifications_swipe_refresh_layout.setOnRefreshListener {\n" +
        "            Handler().postDelayed({\n" +
        "                notifications_swipe_refresh_layout?.isRefreshing = false\n" +
        "            }, ANIMATION_REFRESH_TIMEOUT)\n" +
        "        }\n" +
        "\n" +
        "        view.toolbar_notifications.apply {\n" +
        "\n" +
        "            setOnTouchListener { _, _ ->\n" +
        "\n" +
        "                view.notificationsRecyclerView.requestFocus()\n" +
        "            }\n" +
        "        }\n" +
        "    } // onViewCreated"
}
