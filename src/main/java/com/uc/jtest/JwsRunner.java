package com.uc.jtest;
import java.io.File;

import jws.Invoker;
import jws.Invoker.DirectInvocation;
import jws.Jws;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.uc.jtest.utils.Logger;
import com.uc.jtest.utils.LoggerFactory;

public class JwsRunner {

    private static final Logger logger = LoggerFactory
            .getLogger(JwsRunner.class);

 
    public static void JwsStartForJunit() throws Exception {
        logger.debug("JwsStartForJunit");
        if (!Jws.started) {
            Jws.init(new File("."), "test");
            Jws.javaPath.add(Jws.getVirtualFile("test"));
            Jws.start();
        }
    }

    // *********************
    public enum StartJws implements MethodRule {

        INVOKE_THE_TEST_IN_JWS_CONTEXT {

            public Statement apply(final Statement base,
                    FrameworkMethod method, Object target) {

                return new Statement() {

                    @Override
                    public void evaluate() throws Throwable {
                      if (!Jws.started) {
//                          // Jws.forceProd = true;
                          Jws.init(new File("."), "test");
                      }

                        try {
                            Invoker.invokeInThread(new DirectInvocation() {

                                @Override
                                public void execute() throws Exception {
                                    try {
                                        base.evaluate();
                                    } catch (Throwable e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                                @Override
                                public Invoker.InvocationContext getInvocationContext() {
                                    return new Invoker.InvocationContext(
                                            invocationType);
                                }
                            });
                        } catch (Throwable e) {
                            throw ExceptionUtils.getRootCause(e);
                        }
                    }
                };
            }
        },
        JUST_RUN_THE_TEST {

            public Statement apply(final Statement base,
                    FrameworkMethod method, Object target) {
                return new Statement() {

                    @Override
                    public void evaluate() throws Throwable {
                        base.evaluate();
                    }
                };
            }
        };

        public static StartJws rule(boolean isRunTestInJwsContext) {
            return isRunTestInJwsContext ? INVOKE_THE_TEST_IN_JWS_CONTEXT
                    : JUST_RUN_THE_TEST;
        }
    }



}
