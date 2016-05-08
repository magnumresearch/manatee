'use strict';

describe('Controller Tests', function() {

    describe('Patient Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPatient, MockReferralSource;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPatient = jasmine.createSpy('MockPatient');
            MockReferralSource = jasmine.createSpy('MockReferralSource');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Patient': MockPatient,
                'ReferralSource': MockReferralSource
            };
            createController = function() {
                $injector.get('$controller')("PatientDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'manateeApp:patientUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
