package com.ai.zeld.util.objectpool

import android.graphics.RectF

object RectFPool : SimpleObjectPool<RectF>(50, { RectF() })