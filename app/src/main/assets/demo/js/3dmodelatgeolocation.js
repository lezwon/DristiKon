var World = {
	loaded: false,
	rotating: false,

	init: function initFn() {
		this.createModelAtLocation();
	},

	createModelAtLocation: function createModelAtLocationFn() {

		/*
			First a location where the model should be displayed will be defined. This location will be relativ to the user.	
		*/
		var location = new AR.RelativeLocation(null, -25, 35, -50);

		/*
			Next the model object is loaded.
		*/
		this.modelKnife = new AR.Model("assets/knife.wt3", {
			onLoaded: this.worldLoaded,
            scale: {
                x: 1,
                y: 1,
                z: 1
            },
            onClick: function () {
                if (!World.rotationAnimation.isRunning()) {
                    if (!World.rotating) {
                        // Starting an animation with .start(-1) will loop it indefinitely.
                        World.rotationAnimation.start(-1);
                        World.rotating = true;
                    } else {
                        // Resumes the rotation animation
                        World.rotationAnimation.resume();
                    }
                } else {
                    // Pauses the rotation animation
                    World.rotationAnimation.pause();
                }

                World.close();
                document.location = 'architectsdk://actionButton?action=close';
                return false;
            }
		});

        this.rotationAnimation = new AR.PropertyAnimation(this.modelKnife, "rotate.z", -25, 335, 10000);

        var indicatorImage = new AR.ImageResource("assets/indi.png");

        var indicatorDrawable = new AR.ImageDrawable(indicatorImage, 0.1, {
            verticalAnchor: AR.CONST.VERTICAL_ANCHOR.TOP
        });

		/*
			Putting it all together the location and 3D model is added to an AR.GeoObject.
		*/
		var obj = new AR.GeoObject(location, {
            drawables: {
               cam: [this.modelKnife],
               indicator: [indicatorDrawable]
            }
        });
	},

	worldLoaded: function worldLoadedFn() {
		World.loaded = true;
		var e = document.getElementById('loadingMessage');
		e.parentElement.removeChild(e);
	}
};

World.init();

