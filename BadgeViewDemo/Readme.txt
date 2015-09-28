badgeView——在右上角显示提示数量。常用用法：
		backgroundDrawableBadge = new BadgeView(getActivity());/创建badgeview
        backgroundDrawableBadge.setBadgeCount(88);//设置提示数量
        backgroundDrawableBadge.setBackgroundResource(R.drawable.badge_blue);//设置背景样式
        backgroundShapeBadge.setBackground(12, Color.parseColor("#9b2eef"));//设置背景颜色
        ravityBadge.setBadgeGravity(Gravity.LEFT | Gravity.BOTTOM);//设置提示信息的布局位置
        textStyleBadge.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC));//设置文字的样式和字体
        backgroundDrawableBadge.setTargetView(backgroundDrawableView);//设置目标组件，以便显示在上面