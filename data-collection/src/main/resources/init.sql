create table if not exists DailySalesData (
	id String PRIMARY KEY,
	city String,
	quyu String,
	jiedao String,
	num Integer,
	crawlerDate Date ,
	url String,
	createTime Timestamp
);
create table if not exists DealData (
	id String,
	houseNo String PRIMARY KEY,
	city String,
	quyu String,
	jiedao String,
	village String,
	chaoxiang String,
	louceng String,
	huxing String,
	buildTime Integer,
	buildType String,
	price Double,
	mianji Double,
	totalCost Double,
	dealDate Date,
	url String,
	createTime Timestamp
);